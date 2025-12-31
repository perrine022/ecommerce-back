package com.tradeall.tradefood.controller;

import com.tradeall.tradefood.client.SellsyClient;
import com.tradeall.tradefood.dto.sellsy.SellsyAddressDTO;
import com.tradeall.tradefood.dto.sellsy.SellsyResponse;
import com.tradeall.tradefood.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Contrôleur gérant les adresses des compagnies Sellsy.
 */
@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private static final Logger log = LoggerFactory.getLogger(AddressController.class);
    private final SellsyClient sellsyClient;
    private final UserService userService;

    public AddressController(SellsyClient sellsyClient, UserService userService) {
        this.sellsyClient = sellsyClient;
        this.userService = userService;
    }

    /**
     * Récupère la liste des adresses pour un utilisateur donné.
     */
    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<SellsyResponse<SellsyAddressDTO>>> getUserAddresses(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "25") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        log.info("Récupération des adresses pour l'utilisateur ID: {}", userId);
        return userService.getUserAddresses(userId, limit, offset)
                .map(ResponseEntity::ok);
    }

    /**
     * Crée une nouvelle adresse pour un utilisateur.
     */
    @PostMapping("/user/{userId}")
    public Mono<ResponseEntity<SellsyAddressDTO>> createAddress(
            @PathVariable UUID userId,
            @RequestBody SellsyAddressDTO address) {
        log.info("Création d'une adresse pour l'utilisateur ID: {}", userId);
        return userService.createUserAddress(userId, address)
                .map(ResponseEntity::ok);
    }

    /**
     * Met à jour une adresse existante pour un utilisateur.
     */
    @PutMapping("/user/{userId}/{addressId}")
    public Mono<ResponseEntity<SellsyAddressDTO>> updateUserAddress(
            @PathVariable UUID userId,
            @PathVariable Long addressId,
            @RequestBody SellsyAddressDTO address) {
        log.info("Mise à jour de l'adresse ID: {} pour l'utilisateur ID: {}", addressId, userId);
        return userService.updateUserAddress(userId, addressId, address)
                .map(ResponseEntity::ok);
    }

    /**
     * Supprime une adresse pour un utilisateur.
     */
    @DeleteMapping("/user/{userId}/{addressId}")
    public Mono<ResponseEntity<Void>> deleteUserAddress(
            @PathVariable UUID userId,
            @PathVariable Long addressId) {
        log.info("Suppression de l'adresse ID: {} pour l'utilisateur ID: {}", addressId, userId);
        return userService.deleteUserAddress(userId, addressId)
                .thenReturn(ResponseEntity.ok().<Void>build());
    }

    /**
     * Met à jour une adresse existante pour une compagnie.
     * @deprecated Utiliser les endpoints basés sur l'ID utilisateur.
     */
    @PutMapping("/company/{companyId}/{addressId}")
    @Deprecated
    public Mono<ResponseEntity<SellsyAddressDTO>> updateAddress(
            @PathVariable Long companyId,
            @PathVariable Long addressId,
            @RequestBody SellsyAddressDTO address) {
        log.info("Mise à jour de l'adresse ID: {} pour la compagnie Sellsy ID: {}", addressId, companyId);
        return sellsyClient.updateAddress("company", companyId, addressId, address)
                .map(ResponseEntity::ok);
    }

    /**
     * Supprime une adresse pour une compagnie.
     * @deprecated Utiliser les endpoints basés sur l'ID utilisateur.
     */
    @DeleteMapping("/company/{companyId}/{addressId}")
    @Deprecated
    public Mono<ResponseEntity<Void>> deleteAddress(
            @PathVariable Long companyId,
            @PathVariable Long addressId) {
        log.info("Suppression de l'adresse ID: {} pour la compagnie Sellsy ID: {}", addressId, companyId);
        return sellsyClient.deleteAddress("company", companyId, addressId)
                .thenReturn(ResponseEntity.ok().<Void>build());
    }
}
