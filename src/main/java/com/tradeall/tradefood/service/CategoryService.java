package com.tradeall.tradefood.service;

import com.tradeall.tradefood.client.SellsyClient;
import com.tradeall.tradefood.dto.sellsy.SellsyCategory;
import com.tradeall.tradefood.entity.Category;
import com.tradeall.tradefood.repository.CategoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service gérant les catégories et leur synchronisation avec Sellsy.
 */
@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final SellsyClient sellsyClient;
    private final ObjectMapper objectMapper;

    public CategoryService(CategoryRepository categoryRepository, SellsyClient sellsyClient, ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.sellsyClient = sellsyClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Récupère toutes les catégories.
     * @return Liste des catégories.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Récupère une catégorie par son ID.
     * @param id L'identifiant.
     * @return La catégorie.
     */
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    /**
     * Déclenche la synchronisation des catégories depuis Sellsy.
     */
    @Transactional
    public void syncCategories() {
        log.info("Début de la synchronisation des catégories depuis Sellsy...");
        int limit = 100;
        fetchAndSaveCategories(0, limit);
    }

    private void fetchAndSaveCategories(int offset, int limit) {
        sellsyClient.getCategories(limit, offset).subscribe(
            response -> {
                List<SellsyCategory> sellsyCategories = response.getData();
                log.info("Traitement de {} catégories (offset: {})", sellsyCategories.size(), offset);
                
                for (SellsyCategory sc : sellsyCategories) {
                    Category category = categoryRepository.findBySellsyId(sc.getId())
                            .orElse(new Category());

                    category.setSellsyId(sc.getId());
                    category.setLabel(sc.getLabel());
                    category.setColor(sc.getColor());
                    category.setIcon(sc.getIcon());
                    category.setIsDefault(sc.getIs_default());

                    if (sc.get_embed() != null && sc.get_embed().getSources() != null) {
                        try {
                            category.setSources(objectMapper.writeValueAsString(sc.get_embed().getSources()));
                        } catch (JsonProcessingException e) {
                            log.error("Erreur lors de la sérialisation des sources pour la catégorie {}: {}", sc.getLabel(), e.getMessage());
                        }
                    }

                    categoryRepository.save(category);
                }

                if (response.getPagination() != null && response.getPagination().getTotal() > offset + limit) {
                    fetchAndSaveCategories(offset + limit, limit);
                } else {
                    log.info("Synchronisation des catégories terminée.");
                }
            },
            error -> log.error("Erreur lors de la synchronisation des catégories Sellsy: {}", error.getMessage())
        );
    }
}
