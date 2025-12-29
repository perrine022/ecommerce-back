package com.tradeall.tradefood.repository;

import com.tradeall.tradefood.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
