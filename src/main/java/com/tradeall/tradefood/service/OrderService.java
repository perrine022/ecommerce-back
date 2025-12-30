package com.tradeall.tradefood.service;

import com.tradeall.tradefood.client.SellsyClient;
import com.tradeall.tradefood.dto.sellsy.SellsyOrder;
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
    public void finalizeOrder(UUID orderId) {
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
     * Crée une commande dans le système Sellsy via l'API.
     * @param order La commande locale à synchroniser.
     */
    private void createSellsyOrder(Order order) {
        log.debug("Début de la création de la commande Sellsy pour la commande locale ID: {}", order.getId());
        
        SellsyOrder sellsyOrder = new SellsyOrder();
        
        if (order.getUser().getSellsyContactId() == null) {
            log.error("L'utilisateur {} n'a pas de SellsyContactId. Impossible de créer la commande Sellsy.", order.getUser().getEmail());
            return;
        }
        
        sellsyOrder.setContact_id(Long.parseLong(order.getUser().getSellsyContactId()));
        sellsyOrder.setSubject("Commande Tradefood #" + order.getId());
        sellsyOrder.setDate(LocalDateTime.now().toString());

        log.debug("Préparation des lignes de commande pour Sellsy");
        List<SellsyOrder.SellsyRow> rows = order.getItems().stream()
                .map(item -> {
                    SellsyOrder.SellsyRow row = new SellsyOrder.SellsyRow();
                    row.setType("product");
                    row.setReference(item.getProduct().getReference());
                    row.setQuantity(item.getQuantity().toString());
                    row.setUnit_amount(item.getUnitPrice().toString());
                    row.setDescription(item.getProduct().getName());
                    row.setTax_id(1L); // Valeur par défaut
                    return row;
                })
                .collect(Collectors.toList());
        
        sellsyOrder.setRows(rows);
        
        sellsyClient.createOrder(sellsyOrder).subscribe(
            response -> {
                log.info("Commande Sellsy créée avec succès pour la commande locale ID: {}. Sellsy ID: {}", order.getId(), response.getId());
                order.setSellsyOrderId(response.getId().toString());
                orderRepository.save(order);
            },
            error -> {
                log.error("Erreur lors de la création de la commande Sellsy pour la commande locale ID: {}. Erreur: {}", order.getId(), error.getMessage());
            }
        );
    }

    /**
     * Synchronise les commandes depuis Sellsy pour l'utilisateur.
     * @param user L'utilisateur concerné.
     */
    @Transactional
    public void syncOrdersFromSellsy(User user) {
        log.info("Début de la synchronisation des commandes Sellsy pour l'utilisateur: {}", user.getEmail());
        if (user.getSellsyContactId() == null) {
            log.warn("L'utilisateur {} n'a pas d'ID Sellsy, synchronisation impossible.", user.getEmail());
            return;
        }

        sellsyClient.getOrders(100, 0)
            .map(response -> response.getData().stream()
                .filter(so -> so.getContact_id() != null && 
                             so.getContact_id().toString().equals(user.getSellsyContactId()))
                .collect(Collectors.toList()))
            .subscribe(
                sellsyOrders -> {
                    for (com.tradeall.tradefood.dto.sellsy.SellsyOrder so : sellsyOrders) {
                        if (!orderRepository.existsBySellsyOrderId(so.getId().toString())) {
                            Order newOrder = Order.builder()
                                .user(user)
                                .sellsyOrderId(so.getId().toString())
                                .orderDate(LocalDateTime.now()) 
                                .status(Order.OrderStatus.PAID)
                                .totalAmount(0.0) 
                                .items(new ArrayList<>())
                                .build();
                            orderRepository.save(newOrder);
                        }
                    }
                    log.info("Synchronisation des commandes Sellsy terminée pour {}", user.getEmail());
                },
                error -> log.error("Erreur sync commandes Sellsy pour {}: {}", user.getEmail(), error.getMessage())
            );
    }
}
