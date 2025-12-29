package com.tradeall.tradefood.service;

import com.tradeall.tradefood.client.SellsyClient;
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
        log.debug("Récupération de tous les produits");
        return productRepository.findAll();
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

    /**
     * Synchronise le catalogue local avec les produits de Sellsy.
     */
    @Transactional
    public void syncProducts() {
        log.info("Début de la synchronisation des produits depuis Sellsy");
        sellsyClient.getItems(100, 0)
                .map(response -> {
                    log.debug("Reçu {} produits de Sellsy", response.getData().size());
                    return response.getData().stream()
                        .map(sellsyProduct -> {
                            Product product = Product.builder()
                                .sellsyId(sellsyProduct.getId())
                                .name(sellsyProduct.getName())
                                .reference(sellsyProduct.getReference())
                                .description(sellsyProduct.getDescription())
                                .referencePrice(sellsyProduct.getReference_price())
                                .purchaseAmount(sellsyProduct.getPurchase_amount())
                                .build();
                            
                            // Map all other fields manually or use a more complete builder
                            product.setType(sellsyProduct.getType());
                            product.setReferencePriceTaxesExc(sellsyProduct.getReference_price_taxes_exc());
                            product.setReferencePriceTaxesInc(sellsyProduct.getReference_price_taxes_inc());
                            product.setIsReferencePriceTaxesFree(sellsyProduct.getIs_reference_price_taxes_free());
                            product.setTaxId(sellsyProduct.getTax_id());
                            product.setUnitId(sellsyProduct.getUnit_id());
                            product.setCategoryId(sellsyProduct.getCategory_id());
                            product.setPriceExcl_tax(sellsyProduct.getPrice_excl_tax());
                            product.setCurrency(sellsyProduct.getCurrency());
                            product.setStandardQuantity(sellsyProduct.getStandard_quantity());
                            product.setIsNameIncludedInDescription(sellsyProduct.getIs_name_included_in_description());
                            product.setAccountingCodeId(sellsyProduct.getAccounting_code_id());
                            product.setAccountingPurchaseCodeId(sellsyProduct.getAccounting_purchase_code_id());
                            product.setIsArchived(sellsyProduct.getIs_archived());
                            product.setIsDeclined(sellsyProduct.getIs_declined());
                            product.setIsEinvoicingCompliant(sellsyProduct.getIs_einvoicing_compliant());
                            
                            return product;
                        })
                        .collect(Collectors.toList());
                })
                .subscribe(
                    products -> {
                        productRepository.saveAll(products);
                        log.info("Synchronisation réussie: {} produits mis à jour", products.size());
                    },
                    error -> {
                        log.error("Erreur lors de la synchronisation des produits Sellsy: {}", error.getMessage());
                    }
                );
    }
}
