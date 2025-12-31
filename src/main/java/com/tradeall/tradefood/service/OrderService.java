package com.tradeall.tradefood.service;

import com.tradeall.tradefood.client.SellsyClient;
import com.tradeall.tradefood.dto.sellsy.SellsyOrder;
import com.tradeall.tradefood.dto.sellsy.SellsyOrderRequest;
import com.tradeall.tradefood.entity.Order;
import com.tradeall.tradefood.entity.OrderItem;
import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.repository.OrderRepository;
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

    public OrderService(OrderRepository orderRepository, SellsyClient sellsyClient) {
        this.orderRepository = orderRepository;
        this.sellsyClient = sellsyClient;
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

    /**
     * Crée une commande à partir d'un panier utilisateur.
     * @param cart Le panier source.
     * @return La commande créée en statut PENDING.
     */
    @Transactional
    public Order createOrderFromCart(com.tradeall.tradefood.entity.Cart cart) {
        log.debug("Création d'une commande à partir du panier de l'utilisateur: {}", cart.getUser().getEmail());
        Order order = Order.builder()
                .user(cart.getUser())
                .orderDate(LocalDateTime.now())
                .status(Order.OrderStatus.PENDING)
                .totalAmount(calculateTotal(cart))
                .items(new ArrayList<>())
                .build();

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
        log.info("Commande créée avec succès, ID: {}, Statut: {}", savedOrder.getId(), savedOrder.getStatus());
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
        related.setType(order.getUser().getSellsyType() != null ? order.getUser().getSellsyType() : "contact");
        sellsyOrder.setRelated(Collections.singletonList(related));
        
        sellsyOrder.setSubject("Commande Tradefood #" + order.getId());
        sellsyOrder.setDate(java.time.LocalDate.now().toString());
        
        // Ajout des adresses de facturation et de livraison
        if (order.getInvoicingAddressId() != null) {
            sellsyOrder.setInvoicing_address_id(order.getInvoicingAddressId());
        }
        if (order.getDeliveryAddressId() != null) {
            sellsyOrder.setDelivery_address_id(order.getDeliveryAddressId());
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
        
        sellsyClient.createOrder(sellsyOrder).subscribe(
            response -> {
                log.info("Commande Sellsy créée avec succès pour la commande locale ID: {}. Sellsy ID: {}", order.getId(), response.getId());
                order.setSellsyOrderId(response.getId().toString());
                order.setNumber(response.getNumber());
                orderRepository.save(order);
            },
            error -> {
                log.error("Erreur lors de la création de la commande Sellsy pour la commande locale ID: {}. Erreur: {}", order.getId(), error.getMessage());
            }
        );
    }

    /**
     * Crée manuellement une commande à partir d'un panier (sans Stripe).
     */
    @Transactional
    public Order createManualOrder(com.tradeall.tradefood.entity.Cart cart, com.tradeall.tradefood.dto.OrderCreateRequest request) {
        Order order = createOrderFromCart(cart);
        order.setInvoicingAddressId(request.getInvoicingAddressId());
        order.setDeliveryAddressId(request.getDeliveryAddressId());
        order.setStatus(Order.OrderStatus.PAID); // On la considère payée pour le moment
        Order savedOrder = orderRepository.save(order);
        
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
                "type", "company",
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
    @Transactional
    public void syncOrdersFromSellsy(User user) {
        log.info("Début de la synchronisation des commandes Sellsy pour l'utilisateur: {}", user.getEmail());
        if (user.getSellsyId() == null) {
            log.warn("L'utilisateur {} n'a pas d'ID Sellsy, synchronisation impossible.", user.getEmail());
            return;
        }

        String type = user.getSellsyType() != null ? user.getSellsyType() : "contact";
        
        Map<String, Object> filters = new HashMap<>();
        filters.put("related_objects", Collections.singletonList(Map.of(
                "type", type,
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
