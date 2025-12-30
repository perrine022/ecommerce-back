package com.tradeall.tradefood.controller;

import com.tradeall.tradefood.entity.Category;
import com.tradeall.tradefood.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur gérant les catégories de produits.
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Récupère toutes les catégories.
     * @return Liste des catégories.
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        log.info("Requête pour récupérer toutes les catégories");
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * Récupère une catégorie par son identifiant.
     * @param id L'identifiant de la catégorie.
     * @return La catégorie trouvée.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable UUID id) {
        log.info("Requête pour récupérer la catégorie ID: {}", id);
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    /**
     * Déclenche la synchronisation des catégories depuis Sellsy.
     * @return Réponse vide.
     */
    @PostMapping("/sync")
    public ResponseEntity<Void> syncCategories() {
        log.info("Requête manuelle de synchronisation des catégories Sellsy");
        categoryService.syncCategories();
        return ResponseEntity.ok().build();
    }
}
