package com.tradeall.tradefood.repository;

import com.tradeall.tradefood.entity.CompanySellsy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CompanySellsyRepository extends JpaRepository<CompanySellsy, UUID> {
    Optional<CompanySellsy> findBySellsyId(Long sellsyId);
    Optional<CompanySellsy> findByEmail(String email);
}