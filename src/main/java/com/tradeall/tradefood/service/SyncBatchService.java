package com.tradeall.tradefood.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service gérant les tâches planifiées pour la synchronisation des données.
 */
@Service
public class SyncBatchService {

    private static final Logger log = LoggerFactory.getLogger(SyncBatchService.class);

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;

    public SyncBatchService(ProductService productService, CategoryService categoryService, UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @jakarta.annotation.PostConstruct
    public void initSync() {
        log.info("Lancement de la synchronisation initiale au démarrage...");
        categoryService.syncCategoriesV1();
    }

    /**
     * Exécute la synchronisation complète toutes les nuits à 2h du matin.
     * Cron format: "0 0 2 * * ?"
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void runNightlySync() {
        log.info("Lancement du batch de synchronisation nocturne...");
        
        try {
            log.info("Batch: Synchronisation des catégories...");
            categoryService.syncCategoriesV1();
            
            log.info("Batch: Synchronisation des produits...");
            productService.syncProducts();
            
            log.info("Batch: Synchronisation des utilisateurs (clients)...");
            userService.syncUsers();
            
            log.info("Batch de synchronisation nocturne terminé avec succès.");
        } catch (Exception e) {
            log.error("Erreur lors de l'exécution du batch de synchronisation nocturne: {}", e.getMessage(), e);
        }
    }
}
