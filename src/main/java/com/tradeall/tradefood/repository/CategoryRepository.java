package com.tradeall.tradefood.repository;

import com.tradeall.tradefood.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Category.
 */
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findBySellsyId(Long sellsyId);
}
