package com.tradeall.tradefood.service;

import com.stripe.model.Invoice;
import com.tradeall.tradefood.client.SellsyClient;
import com.tradeall.tradefood.dto.sellsy.SellsyInvoice;
import com.tradeall.tradefood.dto.sellsy.SellsyInvoiceRequest;
import com.tradeall.tradefood.dto.sellsy.SellsyOrder;
import com.tradeall.tradefood.dto.sellsy.SellsyOrderRequest;
import com.tradeall.tradefood.dto.sellsy.SellsyPaymentRequest;
import com.tradeall.tradefood.dto.sellsy.SellsyPaymentResponse;
import com.tradeall.tradefood.dto.sellsy.SellsyLinkPaymentRequest;
import com.tradeall.tradefood.dto.sellsy.SellsyPaymentMethod;
import com.tradeall.tradefood.entity.Order;
import com.tradeall.tradefood.entity.OrderItem;
import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.entity.Product;
import com.tradeall.tradefood.entity.Payment;
import com.tradeall.tradefood.repository.OrderRepository;
import com.tradeall.tradefood.repository.ProductRepository;
import com.tradeall.tradefood.repository.UserRepository;
import com.tradeall.tradefood.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service gérant les commandes et leur synchronisation avec Sellsy.
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final SellsyClient sellsyClient;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public OrderService(OrderRepository orderRepository, SellsyClient sellsyClient, 
                        ProductRepository productRepository, UserRepository userRepository,
                        PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.sellsyClient = sellsyClient;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Récupère toutes les commandes d'un utilisateur donné.
     * @param user L'utilisateur concerné.
     * @return La liste des commandes de l'utilisateur.
     */
    public List<Order> getOrdersByUser(User user) {
        log.debug("Récupération des commandes pour l'utilisateur: {}", user.getEmail());
        return orderRepository.findByUser(user);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        log.info("Récupération des commandes pour l'utilisateur ID: {}", userId);
        return orderRepository.findByUser(User.builder().id(userId).build());
    }

    /**
     * Récupère la liste de toutes les commandes du système.
     * @return La liste de toutes les commandes.
     */
    public List<Order> getAllOrders() {
        log.debug("Récupération de toutes les commandes");
        return orderRepository.findAll();
    }

    /**
     * Récupère une commande par son identifiant unique.
     * @param id L'identifiant de la commande.
     * @return La commande correspondante.
     * @throws RuntimeException si la commande n'est pas trouvée.
     */
    public Order getOrderById(UUID id) {
        log.debug("Récupération de la commande ID: {}", id);
        return orderRepository.findById(id).orElseThrow(() -> {
            log.error("Commande non trouvée pour l'ID: {}", id);
            return new RuntimeException("Order not found");
        });
    }

    private String generateValidationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    /**
     * Valide une commande avec un code à 4 chiffres pour un utilisateur donné.
     */
    @Transactional
    public Order validateOrderWithCode(UUID orderId, UUID userId, String code) {
        Order order = getOrderById(orderId);
        
        if (!order.getUser().getId().equals(userId)) {
            log.warn("Tentative de validation de la commande ID: {} pour un utilisateur incorrect. Attendu: {}, Reçu: {}", 
                    orderId, order.getUser().getId(), userId);
            throw new RuntimeException("La commande n'appartient pas à l'utilisateur spécifié");
        }

        if (order.getValidationCode() != null && order.getValidationCode().equals(code)) {
            order.setValidated(true);
            order.setStatus(Order.OrderStatus.DELIVERED);
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Code de validation incorrect");
        }
    }

    /**
     * Crée une commande à partir d'un panier utilisateur.
     * @param cart Le panier source.
     * @return La commande créée en statut PENDING.
     */
    @Transactional
    public Order createOrderFromCart(com.tradeall.tradefood.entity.Cart cart) {
        log.info("Début de la création d'une commande à partir du panier de l'utilisateur: {}", cart.getUser().getEmail());
        Order order = Order.builder()
                .user(cart.getUser())
                .orderDate(LocalDateTime.now())
                .status(Order.OrderStatus.PENDING)
                .totalAmount(calculateTotal(cart))
                .validationCode(generateValidationCode())
                .items(new ArrayList<>())
                .build();

        log.debug("Conversion de {} articles du panier en articles de commande", cart.getItems().size());
        order.setItems(cart.getItems().stream()
                .map(cartItem -> {
                    Double price = Double.parseDouble(cartItem.getProduct().getReferencePrice() != null ? cartItem.getProduct().getReferencePrice() : "0");
                    return OrderItem.builder()
                        .order(order)
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .unitPrice(price)
                        .build();
                })
                .collect(Collectors.toList()));

        Order savedOrder = orderRepository.save(order);
        log.info("Commande créée à partir du panier avec succès. ID: {}, Statut: {}, Montant: {}", 
                savedOrder.getId(), savedOrder.getStatus(), savedOrder.getTotalAmount());
        return savedOrder;
    }

    /**
     * Calcule le montant total d'un panier.
     * @param cart Le panier.
     * @return Le montant total.
     */
    private Double calculateTotal(com.tradeall.tradefood.entity.Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> {
                    Double price = Double.parseDouble(item.getProduct().getReferencePrice() != null ? item.getProduct().getReferencePrice() : "0");
                    return price * item.getQuantity();
                })
                .sum();
    }

    /**
     * Finalise une commande après paiement (passe le statut à PAID et synchronise avec Sellsy).
     * @param orderId L'identifiant de la commande à finaliser.
     */
    public void finalizeOrder(java.util.UUID orderId) {
        log.debug("Finalisation de la commande ID: {}", orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> {
            log.error("Impossible de finaliser, commande non trouvée ID: {}", orderId);
            return new RuntimeException("Order not found");
        });
        
        order.setStatus(Order.OrderStatus.PAID);
        orderRepository.save(order);
        log.info("Commande ID: {} marquée comme PAYÉE", orderId);
        
        createSellsyOrder(order);
    }

    /**
     * Met à jour les adresses de facturation et de livraison d'une commande.
     * @param orderId L'identifiant de la commande.
     * @param addresses Les nouveaux identifiants d'adresse.
     * @return La commande mise à jour.
     */
    @org.springframework.transaction.annotation.Transactional
    public Order updateOrderAddresses(java.util.UUID orderId, com.tradeall.tradefood.dto.AddressRequestDTO addresses) {
        Order order = getOrderById(orderId);
        order.setInvoicingAddressId(addresses.getInvoicingAddressId());
        order.setDeliveryAddressId(addresses.getDeliveryAddressId());
        return orderRepository.save(order);
    }

    /**
     * Crée une commande dans le système Sellsy via l'API.
     * @param order La commande locale à synchroniser.
     */
    private void createSellsyOrder(Order order) {
        log.debug("Début de la création de la commande Sellsy pour la commande locale ID: {}", order.getId());
        
        SellsyOrderRequest sellsyOrder = new SellsyOrderRequest();
        
        if (order.getUser().getSellsyId() == null) {
            log.error("L'utilisateur {} n'a pas de SellsyId. Impossible de créer la commande Sellsy.", order.getUser().getEmail());
            return;
        }
        
        SellsyOrderRequest.SellsyRelated related = new SellsyOrderRequest.SellsyRelated();
        related.setId(order.getUser().getSellsyId());
        related.setType("company");
        sellsyOrder.setRelated(Collections.singletonList(related));
        
        sellsyOrder.setSubject("Commande Tradefood #" + order.getId());
        sellsyOrder.setDate(java.time.LocalDate.now().toString());
        
        // Ajout des settings Stripe
        SellsyOrderRequest.SellsySettings settings = new SellsyOrderRequest.SellsySettings();
        SellsyOrderRequest.SellsyPayments payments = new SellsyOrderRequest.SellsyPayments();
        payments.setPayment_modules(Collections.singletonList("stripe"));
        payments.setDirect_debit_module("stripe");
        settings.setPayments(payments);
        
        SellsyOrderRequest.SellsyPdfDisplay pdfDisplay = new SellsyOrderRequest.SellsyPdfDisplay();
        settings.setPdf_display(pdfDisplay);
        
        sellsyOrder.setSettings(settings);
        
        // Ajout des adresses de facturation et de livraison
        if (order.getInvoicingAddressId() != null) {
            sellsyOrder.setInvoicing_address_id(order.getInvoicingAddressId());
        }
        if (order.getDeliveryAddressId() != null) {
            sellsyOrder.setDelivery_address_id(order.getDeliveryAddressId());
        }
        if (order.getIssuerAddressId() != null) {
            sellsyOrder.setIssuer_address_id(order.getIssuerAddressId());
        }

        log.debug("Préparation des lignes de commande pour Sellsy");
        List<SellsyOrderRequest.SellsyRowRequest> rows = order.getItems().stream()
                .map(item -> {
                    SellsyOrderRequest.SellsyRowRequest row = new SellsyOrderRequest.SellsyRowRequest();
                    row.setType("single");
                    row.setReference(item.getProduct().getReference());
                    row.setQuantity(item.getQuantity().toString());
                    row.setUnit_amount(item.getUnitPrice().toString());
                    row.setDescription(item.getProduct().getName());
                    row.setTax_id(item.getProduct().getTaxId() != null ? item.getProduct().getTaxId() : 1L);
                    row.setUnit_id(item.getProduct().getUnitId());
                    row.setPurchase_amount(item.getProduct().getPurchaseAmount());
                    row.setAccounting_code_id(item.getProduct().getAccountingCodeId());
                    return row;
                })
                .collect(Collectors.toList());
        
        sellsyOrder.setRows(rows);

        try {
            String jsonPayload = sellsyClient.serializeToJson(sellsyOrder);
            log.info("[SELLSY] Payload envoyé pour la commande locale {}: {}", order.getId(), jsonPayload);
        } catch (Exception e) {
            log.warn("[SELLSY] Impossible de sérialiser le payload pour les logs: {}", e.getMessage());
        }
        
        sellsyClient.createOrder(sellsyOrder).subscribe(
            response -> {
                log.info("Commande Sellsy créée avec succès pour la commande locale ID: {}. Sellsy ID: {}", order.getId(), response.getId());
                order.setSellsyOrderId(response.getId().toString());
                order.setNumber(response.getNumber());
                orderRepository.save(order);
                
                // On récupère l'administrateur pour l'owner_id de la facture
                // L'utilisateur demande maintenant d'utiliser l'id fixe 579469
                Long adminSellsyId = 579469L;

                // Création de la facture Sellsy
                SellsyInvoiceRequest invoiceRequest = new SellsyInvoiceRequest();
                invoiceRequest.setNumber(java.util.UUID.randomUUID().toString());
                invoiceRequest.setSubject("Facture pour Commande Tradefood #" + order.getId());
                
                String currentDate = java.time.LocalDate.now().toString();
                String now = java.time.OffsetDateTime.now().toString();
                
                invoiceRequest.setDate(currentDate);
                invoiceRequest.setDue_date(currentDate);
                invoiceRequest.setCreated(now);
                invoiceRequest.setShipping_date(currentDate);
                invoiceRequest.setShipping_volume("1");
                invoiceRequest.setShipping_weight(new SellsyInvoiceRequest.SellsyShippingWeight("1", "kg"));
                
                invoiceRequest.setOrder_reference(response.getNumber());
                invoiceRequest.setCurrency("EUR");
                invoiceRequest.setVat_mode("debit");
                invoiceRequest.setPublic_link_enabled(true);
                invoiceRequest.setNote("Généré automatiquement par Tradefood");

                invoiceRequest.setOwner_id(adminSellsyId);
                invoiceRequest.setAssigned_staff_id(adminSellsyId);
                
                // Bank account et Payment methods (Stripe)
                invoiceRequest.setBank_account_id(1L); // Valeur par défaut de l'exemple

                // Ajout des adresses (identiques à la commande)
                if (order.getInvoicingAddressId() != null) {
                    invoiceRequest.setInvoicing_address_id(order.getInvoicingAddressId());
                }
                if (order.getDeliveryAddressId() != null) {
                    invoiceRequest.setDelivery_address_id(order.getDeliveryAddressId());
                }

                // Discount par défaut comme dans le payload exemple
                invoiceRequest.setDiscount(new SellsyInvoiceRequest.SellsyDiscount("percent", "0.00"));

                // Récupération dynamique de la méthode de paiement "stripe"
                sellsyClient.getPaymentMethods().subscribe(
                    methodsResponse -> {
                        Long methodId = methodsResponse.getData().stream()
                                .filter(m -> "stripe".equalsIgnoreCase(m.getLabel()))
                                .map(SellsyPaymentMethod::getId)
                                .findFirst()
                                .orElse(7L);
                        invoiceRequest.setPayment_method_ids(Collections.singletonList(methodId));
                        
                        log.info("Méthode de paiement 'stripe' ajoutée à la facture avec l'ID: {}", methodId);
                        
                        // Envoi de la requête une fois les IDs récupérés
                        createSellsyInvoice(order, invoiceRequest);
                    },
                    error -> {
                        log.warn("Impossible de récupérer les méthodes de paiement, utilisation de la valeur par défaut 7L. Erreur: {}", error.getMessage());
                        invoiceRequest.setPayment_method_ids(Collections.singletonList(7L));
                        createSellsyInvoice(order, invoiceRequest);
                    }
                );
            },
            error -> {
                log.error("Erreur lors de la création de la commande Sellsy pour la commande locale ID: {}. Erreur: {}", order.getId(), error.getMessage());
                if (error instanceof org.springframework.web.reactive.function.client.WebClientResponseException) {
                    org.springframework.web.reactive.function.client.WebClientResponseException we = (org.springframework.web.reactive.function.client.WebClientResponseException) error;
                    log.error("[SELLSY] Réponse d'erreur Sellsy: {}", we.getResponseBodyAsString());
                }
            }
        );
    }

    /**
     * Gère la création de la facture Sellsy.
     */
    private void createSellsyInvoice(Order order, SellsyInvoiceRequest invoiceRequest) {
        // Related (Company)
        SellsyInvoiceRequest.SellsyRelated relatedInv = new SellsyInvoiceRequest.SellsyRelated();
        relatedInv.setId(order.getUser().getSellsyId());
        relatedInv.setType("company");
        invoiceRequest.setRelated(Collections.singletonList(relatedInv));

        // Adresses
        if (order.getInvoicingAddressId() != null) {
            invoiceRequest.setInvoicing_address_id(order.getInvoicingAddressId());
        }
        if (order.getDeliveryAddressId() != null) {
            invoiceRequest.setDelivery_address_id(order.getDeliveryAddressId());
        }

        // Lignes de facture (mêmes que la commande)
        List<SellsyInvoiceRequest.SellsyRowRequest> invoiceRows = order.getItems().stream()
                .map(item -> {
                    SellsyInvoiceRequest.SellsyRowRequest row = new SellsyInvoiceRequest.SellsyRowRequest();
                    row.setType("single");
                    row.setReference(item.getProduct().getReference());
                    row.setQuantity(item.getQuantity().toString());
                    row.setUnit_amount(item.getUnitPrice().toString());
                    row.setDescription(item.getProduct().getName());
                    row.setTax_id(item.getProduct().getTaxId() != null ? item.getProduct().getTaxId() : 1L);
                    row.setUnit_id(item.getProduct().getUnitId());
                    row.setPurchase_amount(item.getProduct().getPurchaseAmount());
                    row.setAccounting_code_id(item.getProduct().getAccountingCodeId());
                    row.setDiscount(null);
                    return row;
                })
                .collect(Collectors.toList());
        invoiceRequest.setRows(invoiceRows);

        // Settings Stripe
        SellsyInvoiceRequest.SellsySettings invSettings = new SellsyInvoiceRequest.SellsySettings();
        SellsyInvoiceRequest.SellsyPayments invPayments = new SellsyInvoiceRequest.SellsyPayments();
        invPayments.setPayment_modules(Collections.singletonList("stripe"));
        invPayments.setDirect_debit_module("stripe");
        invSettings.setPayments(invPayments);
        
        // Configuration de l'affichage PDF avec tous les champs à true par défaut
        SellsyInvoiceRequest.SellsyPdfDisplay pdfDisplay = new SellsyInvoiceRequest.SellsyPdfDisplay();
        invSettings.setPdf_display(pdfDisplay);
        
        invoiceRequest.setSettings(invSettings);
        SellsyInvoiceRequest.SellsyParent parent = new SellsyInvoiceRequest.SellsyParent();
        parent.setType("order");
        parent.setId(Long.valueOf(order.getSellsyOrderId()));
        invoiceRequest.setParent(parent);

        try {
            String jsonInvoice = sellsyClient.serializeToJson(invoiceRequest);
            log.info("[SELLSY] Payload Facture envoyé pour la commande locale {}: {}", order.getId(), jsonInvoice);
        } catch (Exception e) {
            log.warn("[SELLSY] Impossible de sérialiser le payload facture pour les logs: {}", e.getMessage());
        }

        sellsyClient.createInvoice(invoiceRequest).subscribe(
            invoiceResponse -> {
                log.info("Facture Sellsy créée avec succès pour la commande locale ID: {}. Facture Sellsy ID: {}", order.getId(), invoiceResponse.getId());
                // On lie le paiement à la facture
                handleSellsyPayment(order, invoiceResponse.getId(), Double.parseDouble(invoiceResponse.getAmounts().getTotal_excl_tax()));
            },
            invError -> {
                log.error("Erreur lors de la création de la facture Sellsy pour la commande locale ID: {}. Erreur: {}", order.getId(), invError.getMessage());
                if (invError instanceof org.springframework.web.reactive.function.client.WebClientResponseException) {
                    org.springframework.web.reactive.function.client.WebClientResponseException we = (org.springframework.web.reactive.function.client.WebClientResponseException) invError;
                    log.error("[SELLSY] Réponse d'erreur (Facture): {}", we.getResponseBodyAsString());
                }
            }
        );
    }

    /**
     * Gère la création et la liaison du paiement dans Sellsy.
     */
    private void handleSellsyPayment(Order order, Long sellsyInvoiceId, Double amount) {
        log.info("Début de la gestion du paiement Sellsy pour la facture: {}", sellsyInvoiceId);
        
        // Recherche d'un paiement Stripe récent pour cet utilisateur
        List<Payment> userPayments = paymentRepository.findByUserId(order.getUser().getId());
        String stripeId = "Non spécifié";
        if (!userPayments.isEmpty()) {
            // On prend le plus récent
            Payment lastPayment = userPayments.stream()
                    .sorted(Comparator.comparing(Payment::getPaymentDate).reversed())
                    .findFirst()
                    .orElse(null);
            if (lastPayment != null && lastPayment.getPaymentIntent() != null) {
                stripeId = lastPayment.getPaymentIntent();
                log.debug("ID Stripe trouvé pour le paiement Sellsy: {}", stripeId);
            }
        }

        final String finalStripeId = stripeId;

        // Récupération dynamique de la méthode de paiement "stripe"
        sellsyClient.getPaymentMethods().flatMap(methodsResponse -> {
            Long methodId = methodsResponse.getData().stream()
                    .filter(m -> "stripe".equalsIgnoreCase(m.getLabel()))
                    .map(SellsyPaymentMethod::getId)
                    .findFirst()
                    .orElse(7L); // Valeur par défaut si non trouvé (à adapter si besoin)
            
            log.info("Méthode de paiement 'stripe' trouvée avec l'ID: {}", methodId);

            SellsyPaymentRequest paymentRequest = new SellsyPaymentRequest();
            paymentRequest.setNumber("PAY-" + order.getId());
            paymentRequest.setPaid_at(java.time.OffsetDateTime.now().toString());
            paymentRequest.setPayment_method_id(methodId.intValue());
            paymentRequest.setType("credit");
            paymentRequest.setNote("Paiement Stripe: " + finalStripeId);
            
            SellsyPaymentRequest.SellsyAmount sellsyAmount = new SellsyPaymentRequest.SellsyAmount();
            sellsyAmount.setValue(String.format(Locale.US, "%.2f", amount));
            sellsyAmount.setCurrency("EUR");
            paymentRequest.setAmount(sellsyAmount);

            Long sellsyUserId = order.getUser().getSellsyId();
            String sellsyType = order.getUser().getSellsyType(); // "company" ou "individual"
            
            if ("individual".equalsIgnoreCase(sellsyType)) {
                return sellsyClient.createPaymentForIndividual(sellsyUserId, paymentRequest);
            } else {
                return sellsyClient.createPaymentForCompany(sellsyUserId, paymentRequest);
            }
        }).flatMap(paymentResponse -> {
            log.info("Paiement Sellsy créé avec succès. ID: {}. Liaison à la facture: {}", paymentResponse.getId(), sellsyInvoiceId);
            
            // Récupération de la liste des factures avant la liaison (demande utilisateur)
            return sellsyClient.getInvoices(50, 0)
                .doOnNext(invoicesResponse -> {
                    log.info("[SELLSY] Liste des factures récupérée avant liaison (Total: {})", 
                        invoicesResponse.getPagination() != null ? invoicesResponse.getPagination().getTotal() : "inconnu");
                    invoicesResponse.getData().forEach(inv -> 
                        log.info("[SELLSY] Facture trouvée - ID: {}, Numéro: {}, Statut: {}, Montant: {}", 
                            inv.getId(), inv.getNumber(), inv.getStatus(), 
                            inv.getAmounts() != null ? inv.getAmounts().getTotal_incl_tax() : "N/A")
                    );
                })
                .flatMap(invoicesResponse -> {
                    SellsyLinkPaymentRequest linkRequest = new SellsyLinkPaymentRequest();
                    linkRequest.setAmount(amount);
                    return sellsyClient.linkPaymentToInvoice(sellsyInvoiceId, paymentResponse.getId(), linkRequest);
                });
        }).subscribe(
            success -> log.info("Paiement lié avec succès à la facture Sellsy ID: {}", sellsyInvoiceId),
            error -> {
                log.error("Erreur lors de la gestion du paiement Sellsy pour la facture {}: {}", sellsyInvoiceId, error.getMessage());
                if (error instanceof org.springframework.web.reactive.function.client.WebClientResponseException) {
                    org.springframework.web.reactive.function.client.WebClientResponseException we = (org.springframework.web.reactive.function.client.WebClientResponseException) error;
                    log.error("[SELLSY] Réponse d'erreur (Paiement/Liaison): {}", we.getResponseBodyAsString());
                }
            }
        );
    }

    /**
     * Crée manuellement une commande à partir d'une liste de produits et quantités.
     */
    @Transactional
    public Order createManualOrder(User authenticatedUser, com.tradeall.tradefood.dto.OrderCreateRequest request) {
        log.info("Début de la création manuelle d'une commande par l'utilisateur: {}", authenticatedUser.getEmail());
        User orderUser = authenticatedUser;

        // Si c'est un commercial qui passe commande pour un client
        if (authenticatedUser.getRole() == User.Role.ROLE_COMMERCIAL && request.getClientId() != null) {
            log.info("Commercial {} crée une commande pour le client ID: {}", authenticatedUser.getEmail(), request.getClientId());
            User client = userRepository.findById(request.getClientId())
                    .orElseThrow(() -> {
                        log.error("Client non trouvé avec l'ID: {}", request.getClientId());
                        return new RuntimeException("Client non trouvé : " + request.getClientId());
                    });
            
            // Vérifier que le client est bien rattaché au commercial (optionnel mais recommandé pour la sécurité)
            if (client.getCommercial() == null || !client.getCommercial().getId().equals(authenticatedUser.getId())) {
                log.error("Le client {} n'est pas rattaché au commercial {}", client.getEmail(), authenticatedUser.getEmail());
                throw new RuntimeException("Le client n'est pas rattaché à ce commercial");
            }
            orderUser = client;
        }

        Order order = new Order();
        order.setUser(orderUser);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PAID);
        order.setInvoicingAddressId(request.getInvoicingAddressId());
        order.setDeliveryAddressId(request.getDeliveryAddressId());
        order.setValidationCode(generateValidationCode());
        
        List<OrderItem> items = new ArrayList<>();
        double total = 0;
        
        log.info("Traitement de {} articles pour la commande", request.getItems().size());
        for (com.tradeall.tradefood.dto.OrderCreateRequest.ProductItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> {
                        log.error("Produit non trouvé avec l'ID: {}", itemReq.getProductId());
                        return new RuntimeException("Produit non trouvé : " + itemReq.getProductId());
                    });
            
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            
            // On récupère le prix du produit (gestion du format String vers Double)
            double price = 0;
            try {
                price = Double.parseDouble(product.getPriceExclTax());
            } catch (Exception e) {
                log.warn("Prix invalide pour le produit {}: {}", product.getName(), product.getPriceExclTax());
            }
            item.setUnitPrice(price);
            
            items.add(item);
            total += price * itemReq.getQuantity();
            log.debug("Article ajouté: {} (Quantité: {}, Prix Unitaire: {})", product.getName(), itemReq.getQuantity(), price);
        }
        
        order.setItems(items);
        order.setTotalAmount(total);
        
        Order savedOrder = orderRepository.save(order);
        log.info("Commande manuelle créée avec succès. ID: {}, Utilisateur: {}, Montant Total: {}", 
                savedOrder.getId(), orderUser.getEmail(), total);
        createSellsyOrder(savedOrder);
        return savedOrder;
    }

    /**
     * Recherche des commandes Sellsy pour une entreprise donnée.
     * @param sellsyCompanyId L'ID Sellsy de l'entreprise.
     * @return Un Mono contenant la réponse paginée.
     */
    public reactor.core.publisher.Mono<com.tradeall.tradefood.dto.sellsy.SellsyResponse<SellsyOrder>> searchOrdersByCompany(Long sellsyCompanyId) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("related_objects", Collections.singletonList(Map.of(
                "type", "client",
                "id", sellsyCompanyId
        )));
        
        Map<String, Object> body = new HashMap<>();
        body.put("filters", filters);
        
        return sellsyClient.searchOrders(body);
    }

    /**
     * Synchronise les commandes depuis Sellsy pour l'utilisateur.
     * @param user L'utilisateur concerné.
     */
    public void syncOrdersFromSellsy(User user) {
        log.info("Début de la synchronisation des commandes Sellsy pour l'utilisateur: {}", user.getEmail());
        if (user.getSellsyId() == null) {
            log.warn("L'utilisateur {} n'a pas d'ID Sellsy, synchronisation impossible.", user.getEmail());
            return;
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put("related_objects", Collections.singletonList(Map.of(
                "type", "company",
                "id", user.getSellsyId()
        )));
        
        Map<String, Object> body = new HashMap<>();
        body.put("filters", filters);

        sellsyClient.searchOrders(body)
            .subscribe(
                response -> {
                    List<com.tradeall.tradefood.dto.sellsy.SellsyOrder> sellsyOrders = response.getData();
                    for (com.tradeall.tradefood.dto.sellsy.SellsyOrder so : sellsyOrders) {
                        if (!orderRepository.existsBySellsyOrderId(so.getId().toString())) {
                            Order newOrder = Order.builder()
                                .user(user)
                                .sellsyOrderId(so.getId().toString())
                                .orderDate(LocalDateTime.now()) // Idéalement parser so.getCreated()
                                .status(Order.OrderStatus.PAID)
                                .totalAmount(Double.parseDouble(so.getAmounts().getTotal_incl_tax()))
                                .items(new ArrayList<>())
                                .build();
                            newOrder.setNumber(so.getNumber());
                            orderRepository.save(newOrder);
                        }
                    }
                    log.info("Synchronisation des commandes Sellsy terminée pour {}", user.getEmail());
                },
                error -> log.error("Erreur sync commandes Sellsy pour {}: {}", user.getEmail(), error.getMessage())
            );
    }
}
