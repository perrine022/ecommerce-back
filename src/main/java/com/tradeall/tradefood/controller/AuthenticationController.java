package com.tradeall.tradefood.controller;

import com.tradeall.tradefood.dto.auth.AuthenticationRequest;
import com.tradeall.tradefood.dto.auth.AuthenticationResponse;
import com.tradeall.tradefood.dto.auth.RegisterRequest;
import com.tradeall.tradefood.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur gérant les endpoints d'authentification, d'inscription et de gestion du mot de passe.
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    /**
     * Endpoint pour l'inscription d'un nouvel utilisateur.
     * @param request Les données d'inscription.
     * @return Le jeton JWT en cas de succès.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        log.info("Requête d'inscription pour l'email: {}", request.getEmail());
        return ResponseEntity.ok(service.register(request));
    }

    /**
     * Endpoint pour l'authentification d'un utilisateur existant.
     * @param request Les identifiants de connexion.
     * @return Le jeton JWT en cas de succès.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        log.info("Requête d'authentification pour l'email: {}", request.getEmail());
        return ResponseEntity.ok(service.authenticate(request));
    }

    /**
     * Endpoint pour demander la réinitialisation du mot de passe (oubli).
     * @param email L'adresse email de l'utilisateur.
     * @return Réponse vide.
     */
    @PostMapping("/forget-password")
    public ResponseEntity<Void> forgetPassword(@RequestParam String email) {
        log.info("Requête d'oubli de mot de passe pour l'email: {}", email);
        service.forgetPassword(email);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint pour réinitialiser le mot de passe à l'aide d'un jeton.
     * @param token Le jeton de réinitialisation.
     * @param newPassword Le nouveau mot de passe.
     * @return Réponse vide.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        log.info("Requête de réinitialisation de mot de passe avec jeton");
        service.resetPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }
}
