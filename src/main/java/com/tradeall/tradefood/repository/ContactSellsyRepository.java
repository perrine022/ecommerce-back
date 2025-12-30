package com.tradeall.tradefood.repository;

import com.tradeall.tradefood.entity.ContactSellsy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ContactSellsyRepository extends JpaRepository<ContactSellsy, UUID> {
    Optional<ContactSellsy> findBySellsyId(Long sellsyId);
    Optional<ContactSellsy> findByEmail(String email);
}