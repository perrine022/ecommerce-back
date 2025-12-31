package com.tradeall.tradefood.repository;

import com.tradeall.tradefood.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
    List<Product> findByCategoryId(Long categoryId);
}
