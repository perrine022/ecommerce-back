package com.tradeall.tradefood.controller;

import com.tradeall.tradefood.client.SellsyClient;
import com.tradeall.tradefood.dto.sellsy.SellsyAddressDTO;
import com.tradeall.tradefood.dto.sellsy.SellsyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Contrôleur gérant les adresses des compagnies Sellsy.
 */
@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private static final Logger log = LoggerFactory.getLogger(AddressController.class);
    private final SellsyClient sellsyClient;

    public AddressController(SellsyClient sellsyClient) {
        this.sellsyClient = sellsyClient;
    }

    /**
     * Récupère la liste des adresses pour une compagnie donnée.
     */
    @GetMapping("/company/{companyId}")
    public Mono<ResponseEntity<SellsyResponse<SellsyAddressDTO>>> getCompanyAddresses(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "25") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        log.info("Récupération des adresses pour la compagnie Sellsy ID: {}", companyId);
        return sellsyClient.getCompanyAddresses(companyId, limit, offset)
                .map(ResponseEntity::ok);
    }

    /**
     * Crée une nouvelle adresse pour une compagnie.
     */
    @PostMapping("/company/{companyId}")
    public Mono<ResponseEntity<SellsyAddressDTO>> createAddress(
            @PathVariable Long companyId,
            @RequestBody SellsyAddressDTO address) {
        log.info("Création d'une adresse pour la compagnie Sellsy ID: {}", companyId);
        return sellsyClient.createCompanyAddress(companyId, address)
                .map(ResponseEntity::ok);
    }

    /**
     * Met à jour une adresse existante pour une compagnie.
     */
    @PutMapping("/company/{companyId}/{addressId}")
    public Mono<ResponseEntity<SellsyAddressDTO>> updateAddress(
            @PathVariable Long companyId,
            @PathVariable Long addressId,
            @RequestBody SellsyAddressDTO address) {
        log.info("Mise à jour de l'adresse ID: {} pour la compagnie Sellsy ID: {}", addressId, companyId);
        return sellsyClient.updateCompanyAddress(companyId, addressId, address)
                .map(ResponseEntity::ok);
    }

    /**
     * Supprime une adresse pour une compagnie.
     */
    @DeleteMapping("/company/{companyId}/{addressId}")
    public Mono<ResponseEntity<Void>> deleteAddress(
            @PathVariable Long companyId,
            @PathVariable Long addressId) {
        log.info("Suppression de l'adresse ID: {} pour la compagnie Sellsy ID: {}", addressId, companyId);
        return sellsyClient.deleteCompanyAddress(companyId, addressId)
                .thenReturn(ResponseEntity.ok().<Void>build());
    }
}
