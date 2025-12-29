package com.tradeall.tradefood.repository;

import com.tradeall.tradefood.entity.Order;
import com.tradeall.tradefood.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
}
