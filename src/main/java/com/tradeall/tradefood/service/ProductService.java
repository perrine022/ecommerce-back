package com.tradeall.tradefood.service;

import com.tradeall.tradefood.client.SellsyClient;
import com.tradeall.tradefood.dto.sellsy.SellsyProduct;
import com.tradeall.tradefood.entity.Product;
import com.tradeall.tradefood.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service gérant le catalogue produit et la synchronisation avec Sellsy.
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final SellsyClient sellsyClient;

    public ProductService(ProductRepository productRepository, SellsyClient sellsyClient) {
        this.productRepository = productRepository;
        this.sellsyClient = sellsyClient;
    }

    /**
     * Récupère tous les produits disponibles localement.
     * @return Liste des produits.
     */
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info("Récupération de tous les produits : {} trouvés", products.size());
        return products;
    }

    /**
     * Recherche des produits par nom ou description.
     * @param query Le terme de recherche.
     * @return Liste des produits correspondants.
     */
    public List<Product> searchProducts(String query) {
        log.info("Recherche de produits avec la requête: {}", query);
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }

    /**
     * Récupère les produits d'une catégorie donnée.
     * @param categoryId L'ID Sellsy de la catégorie.
     * @return Liste des produits.
     */
    public List<Product> getProductsByCategory(Long categoryId) {
        log.info("Récupération des produits pour la catégorie Sellsy ID: {}", categoryId);
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * Récupère un produit par son identifiant unique.
     * @param id L'identifiant du produit.
     * @return Le produit trouvé.
     * @throws RuntimeException si le produit n'est pas trouvé.
     */
    public Product getProductById(UUID id) {
        log.debug("Récupération du produit ID: {}", id);
        return productRepository.findById(id).orElseThrow(() -> {
            log.error("Produit non trouvé ID: {}", id);
            return new RuntimeException("Product not found");
        });
    }

    /**
     * Crée un nouveau produit localement.
     * @param product Le produit à créer.
     * @return Le produit enregistré.
     */
    @Transactional
    public Product createProduct(Product product) {
        log.debug("Création d'un nouveau produit: {}", product.getName());
        return productRepository.save(product);
    }

    /**
     * Met à jour les informations d'un produit existant.
     * @param id L'identifiant du produit.
     * @param productDetails Les nouveaux détails du produit.
     * @return Le produit mis à jour.
     */
    @Transactional
    public Product updateProduct(UUID id, Product productDetails) {
        log.debug("Mise à jour du produit ID: {}", id);
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setReference(productDetails.getReference());
        product.setDescription(productDetails.getDescription());
        product.setReferencePrice(productDetails.getReferencePrice());
        product.setPurchaseAmount(productDetails.getPurchaseAmount());
        product.setImageUrl(productDetails.getImageUrl());
        return productRepository.save(product);
    }

    /**
     * Supprime un produit du catalogue local.
     * @param id L'identifiant du produit.
     */
    @Transactional
    public void deleteProduct(UUID id) {
        log.debug("Suppression du produit ID: {}", id);
        productRepository.deleteById(id);
    }

    public void syncProducts() {
        log.info("Début de la synchronisation des produits depuis Sellsy...");
        int limit = 100;
        fetchAndSaveProducts(0, limit);
    }

    private void fetchAndSaveProducts(int offset, int limit) {
        sellsyClient.getItems(limit, offset).subscribe(
            response -> {
                List<SellsyProduct> sellsyProducts = response.getData();
                log.info("Traitement de {} produits (offset: {})", sellsyProducts.size(), offset);
                
                for (SellsyProduct sp : sellsyProducts) {
                    Product product = productRepository.findAll().stream()
                            .filter(p -> p.getSellsyId() != null && p.getSellsyId().equals(sp.getId()))
                            .findFirst()
                            .orElse(new Product());

                    product.setSellsyId(sp.getId());
                    product.setName(sp.getName());
                    product.setReference(sp.getReference());
                    product.setDescription(sp.getDescription());
                    product.setType(sp.getType());
                    product.setReferencePrice(sp.getReference_price());
                    product.setReferencePriceTaxesExc(sp.getReference_price_taxes_exc());
                    product.setPurchaseAmount(sp.getPurchase_amount());
                    product.setReferencePriceTaxesInc(sp.getReference_price_taxes_inc());
                    product.setIsReferencePriceTaxesFree(sp.getIs_reference_price_taxes_free());
                    product.setTaxId(sp.getTax_id());
                    product.setUnitId(sp.getUnit_id());
                    product.setCategoryId(sp.getCategory_id());
                    product.setPriceExclTax(sp.getPrice_excl_tax());
                    product.setCurrency(sp.getCurrency());
                    product.setStandardQuantity(sp.getStandard_quantity());
                    product.setIsNameIncludedInDescription(sp.getIs_name_included_in_description());
                    product.setAccountingCodeId(sp.getAccounting_code_id());
                    product.setAccountingPurchaseCodeId(sp.getAccounting_purchase_code_id());
                    product.setIsArchived(sp.getIs_archived());
                    product.setIsDeclined(sp.getIs_declined());
                    product.setIsEinvoicingCompliant(sp.getIs_einvoicing_compliant());
                    
                    if (product.getImageUrl() == null) {
                        product.setImageUrl("https://via.placeholder.com/150");
                    }

                    productRepository.save(product);
                }

                if (response.getPagination() != null && response.getPagination().getTotal() > offset + limit) {
                    fetchAndSaveProducts(offset + limit, limit);
                } else {
                    log.info("Synchronisation des produits terminée.");
                }
            },
            error -> log.error("Erreur lors de la synchronisation des produits Sellsy: {}", error.getMessage())
        );
    }
}
