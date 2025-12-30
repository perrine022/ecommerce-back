package com.tradeall.tradefood.repository;

import com.tradeall.tradefood.entity.IndividualSellsy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface IndividualSellsyRepository extends JpaRepository<IndividualSellsy, UUID> {
    Optional<IndividualSellsy> findBySellsyId(Long sellsyId);
    Optional<IndividualSellsy> findByEmail(String email);
}