package com.tradeall.tradefood.repository;

import com.tradeall.tradefood.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
