package com.tradeall.tradefood.controller;

import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur gérant les utilisateurs : profil personnel et administration.
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        log.info("Requête pour récupérer l'utilisateur actuel: {}", user.getEmail());
        // Recharger l'utilisateur depuis la DB pour éviter les problèmes de Proxy Hibernate hors session
        return ResponseEntity.ok(userService.getUserById(user.getId()));
    }

    /**
     * Récupère le profil de l'utilisateur authentifié.
     * @param user L'utilisateur authentifié.
     * @return Le profil utilisateur.
     */
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal User user) {
        log.info("Requête pour récupérer le profil de l'utilisateur: {}", user.getEmail());
        return ResponseEntity.ok(userService.getProfile(user.getEmail()));
    }

    /**
     * Met à jour le profil de l'utilisateur authentifié.
     * @param user L'utilisateur authentifié.
     * @param userDetails Les nouvelles informations.
     * @return Le profil mis à jour.
     */
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@AuthenticationPrincipal User user, @RequestBody User userDetails) {
        log.info("Requête pour mettre à jour le profil de l'utilisateur: {}", user.getEmail());
        return ResponseEntity.ok(userService.updateUser(user.getId(), userDetails));
    }

    @GetMapping("/commercial/clients")
    @PreAuthorize("hasRole('COMMERCIAL')")
    public ResponseEntity<List<User>> getMyClients(@AuthenticationPrincipal User commercial) {
        log.info("Requête du commercial {} pour récupérer ses clients rattachés", commercial.getEmail());
        return ResponseEntity.ok(userService.getClientsByCommercial(commercial.getId()));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<User>> getUsersByOwner(@PathVariable Long ownerId) {
        log.info("Requête pour récupérer les utilisateurs rattachés au owner ID: {}", ownerId);
        return ResponseEntity.ok(userService.getUsersByOwnerId(ownerId));
    }

    /**
     * Récupère la liste de tous les utilisateurs (Admin seulement).
     * @return Liste des utilisateurs.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Requête Admin pour récupérer tous les utilisateurs");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Supprime un utilisateur (Admin seulement).
     * @param id L'identifiant de l'utilisateur à supprimer.
     * @return Réponse vide.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("Requête Admin pour supprimer l'utilisateur ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Déclenche la synchronisation des clients depuis Sellsy.
     * @return Réponse vide.
     */
    @PostMapping("/sync")
    public ResponseEntity<Void> syncUsers() {
        log.info("Requête manuelle de synchronisation des clients Sellsy");
        userService.syncUsers();
        return ResponseEntity.ok().build();
    }

    /**
     * Déclenche spécifiquement la synchronisation des entreprises (Companies) depuis Sellsy.
     * @return Réponse vide.
     */
    @PostMapping("/sync/companies")
    public ResponseEntity<Void> syncCompanies() {
        log.info("Requête manuelle de synchronisation des entreprises Sellsy");
        userService.syncCompanies();
        return ResponseEntity.ok().build();
    }

    /**
     * Déclenche spécifiquement la synchronisation du staff depuis Sellsy.
     * @return Réponse vide.
     */
    @PostMapping("/sync/staffs")
    public ResponseEntity<Void> syncStaffs() {
        log.info("Requête manuelle de synchronisation du staff Sellsy");
        userService.syncStaffs();
        return ResponseEntity.ok().build();
    }

    /**
     * Récupère les adresses de l'utilisateur.
     */
    @GetMapping("/addresses")
    public reactor.core.publisher.Mono<ResponseEntity<?>> getAddresses(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        log.info("Récupération des adresses pour l'utilisateur: {}", user.getEmail());
        return userService.getUserAddresses(user.getId(), limit, offset)
                .map(response -> {
                    try {
                        String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(response.getData());
                        log.info("Adresses récupérées pour {}: {}", user.getEmail(), json);
                    } catch (Exception e) {
                        log.warn("Impossible de loguer les adresses: {}", e.getMessage());
                    }
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * Crée une nouvelle adresse pour l'utilisateur.
     */
    @PostMapping("/addresses")
    public reactor.core.publisher.Mono<ResponseEntity<?>> createAddress(
            @AuthenticationPrincipal User user,
            @RequestBody com.tradeall.tradefood.dto.sellsy.SellsyAddressDTO addressDTO) {
        log.info("Création d'une adresse pour l'utilisateur: {}", user.getEmail());
        return userService.createUserAddress(user.getId(), addressDTO)
                .map(ResponseEntity::ok);
    }

    /**
     * Met à jour une adresse existante de l'utilisateur.
     */
    @PutMapping("/addresses/{addressId}")
    public reactor.core.publisher.Mono<ResponseEntity<?>> updateAddress(
            @AuthenticationPrincipal User user,
            @PathVariable Long addressId,
            @RequestBody com.tradeall.tradefood.dto.sellsy.SellsyAddressDTO addressDTO) {
        log.info("Mise à jour de l'adresse ID: {} pour l'utilisateur: {}", addressId, user.getEmail());
        return userService.updateUserAddress(user.getId(), addressId, addressDTO)
                .map(ResponseEntity::ok);
    }

    /**
     * Supprime une adresse de l'utilisateur.
     */
    @DeleteMapping("/addresses/{addressId}")
    public reactor.core.publisher.Mono<ResponseEntity<?>> deleteAddress(
            @AuthenticationPrincipal User user,
            @PathVariable Long addressId) {
        log.info("Suppression de l'adresse ID: {} pour l'utilisateur: {}", addressId, user.getEmail());
        return userService.deleteUserAddress(user.getId(), addressId)
                .then(reactor.core.publisher.Mono.just(ResponseEntity.ok().build()));
    }
}
