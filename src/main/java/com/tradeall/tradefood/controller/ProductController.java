package com.tradeall.tradefood.controller;

import com.tradeall.tradefood.entity.Product;
import com.tradeall.tradefood.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur gérant le catalogue de produits et leur synchronisation.
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Récupère la liste de tous les produits.
     * @param search Terme de recherche optionnel.
     * @return Liste des produits.
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId) {
        if (search != null && !search.isEmpty()) {
            log.info("Requête pour rechercher des produits avec: {}", search);
            return ResponseEntity.ok(productService.searchProducts(search));
        }
        if (categoryId != null) {
            log.info("Requête pour récupérer les produits de la catégorie: {}", categoryId);
            return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
        }
        log.info("Requête pour récupérer tous les produits");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Récupère un produit par son identifiant.
     * @param id L'identifiant du produit.
     * @return Le produit trouvé.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        log.info("Requête pour récupérer le produit ID: {}", id);
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Crée un nouveau produit.
     * @param product Les données du produit.
     * @return Le produit créé.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        log.info("Requête pour créer un nouveau produit: {}", product.getName());
        return ResponseEntity.ok(productService.createProduct(product));
    }

    /**
     * Met à jour un produit existant.
     * @param id L'identifiant du produit.
     * @param product Les nouvelles données.
     * @return Le produit mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        log.info("Requête pour mettre à jour le produit ID: {}", id);
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    /**
     * Supprime un produit.
     * @param id L'identifiant du produit.
     * @return Réponse vide.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        log.info("Requête pour supprimer le produit ID: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Déclenche la synchronisation des produits depuis Sellsy.
     * @return Réponse vide.
     */
    @PostMapping("/sync")
    public ResponseEntity<Void> syncProducts() {
        log.info("Requête manuelle de synchronisation des produits Sellsy");
        productService.syncProducts();
        return ResponseEntity.ok().build();
    }
}
