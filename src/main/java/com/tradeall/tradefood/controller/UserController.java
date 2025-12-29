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

    /**
     * Récupère la liste de tous les utilisateurs (Admin seulement).
     * @return Liste des utilisateurs.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Requête Admin pour supprimer l'utilisateur ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
