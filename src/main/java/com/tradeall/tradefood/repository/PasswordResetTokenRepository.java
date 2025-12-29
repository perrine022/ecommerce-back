package com.tradeall.tradefood.repository;

import com.tradeall.tradefood.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);
}
