package com.tradeall.tradefood.service;

import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Composant gérant la synchronisation périodique des données avec Sellsy.
 */
@Component
public class SellsySyncBatch {

    private static final Logger log = LoggerFactory.getLogger(SellsySyncBatch.class);

    private final UserService userService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    public SellsySyncBatch(UserService userService, OrderService orderService, UserRepository userRepository) {
        this.userService = userService;
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    /**
     * Déclenche la synchronisation au démarrage de l'application.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("DÉMARRAGE DE LA MISE À JOUR FORCÉE DES RÔLES UTILISATEURS...");
        try {
            List<User> users = userRepository.findAll();
            int updatedCount = 0;
            for (User user : users) {
                try {
                    user.setRole(User.Role.ROLE_CLIENT);
                    user.setActive(true);
                    userRepository.save(user);
                    updatedCount++;
                } catch (Exception e) {
                    log.error("Impossible de mettre à jour l'utilisateur {}: {}", user.getEmail(), e.getMessage());
                }
            }
            log.info("Mise à jour terminée : {}/{} utilisateurs passés en ROLE_CLIENT.", updatedCount, users.size());

            log.info("Démarrage de la synchronisation Sellsy post-initialisation...");
            syncAll();
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour initiale/synchronisation: {}", e.getMessage(), e);
        }
    }

    /**
     * Batch nocturne : s'exécute tous les jours à 2h du matin.
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void nightlySync() {
        log.info("Démarrage du batch de synchronisation nocturne Sellsy...");
        syncAll();
    }

    private void syncAll() {
        try {
            // 1. Synchronisation des utilisateurs (Contacts, Individuals, Companies)
            userService.syncUsers();
            
            // 2. Synchronisation des commandes pour tous les utilisateurs ayant un ID Sellsy
            List<User> users = userRepository.findAll();
            for (User user : users) {
                if (user.getSellsyId() != null) {
                    orderService.syncOrdersFromSellsy(user);
                }
            }
            
            log.info("Synchronisation globale Sellsy terminée avec succès.");
        } catch (Exception e) {
            log.error("Erreur lors de la synchronisation globale Sellsy: {}", e.getMessage(), e);
        }
    }
}
