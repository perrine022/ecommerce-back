package com.tradeall.tradefood.service;

import com.tradeall.tradefood.dto.auth.AuthenticationRequest;
import com.tradeall.tradefood.dto.auth.AuthenticationResponse;
import com.tradeall.tradefood.dto.auth.RegisterRequest;
import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.repository.UserRepository;
import com.tradeall.tradefood.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.time.LocalDateTime;

/**
 * Service gérant l'authentification, l'inscription et la gestion des mots de passe.
 * Inclut la synchronisation avec Sellsy lors de l'inscription.
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final com.tradeall.tradefood.client.SellsyClient sellsyClient;
    private final com.tradeall.tradefood.repository.PasswordResetTokenRepository tokenRepository;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, com.tradeall.tradefood.client.SellsyClient sellsyClient, com.tradeall.tradefood.repository.PasswordResetTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.sellsyClient = sellsyClient;
        this.tokenRepository = tokenRepository;
    }

    /**
     * Génère un jeton de réinitialisation de mot de passe et l'enregistre.
     * @param email L'adresse email de l'utilisateur.
     */
    public void forgetPassword(String email) {
        log.debug("Demande d'oubli de mot de passe pour l'email: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("Utilisateur non trouvé pour l'email: {}", email);
            return new RuntimeException("User not found");
        });

        String token = UUID.randomUUID().toString();
        com.tradeall.tradefood.entity.PasswordResetToken myToken = new com.tradeall.tradefood.entity.PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(2));
        tokenRepository.save(myToken);
        
        log.debug("Jeton de réinitialisation généré pour {}: {}", email, token);
        // Ici on devrait envoyer un email, mais on log pour le debug
        System.out.println("DEBUG: Reset token for " + email + " is " + token);
    }

    /**
     * Réinitialise le mot de passe de l'utilisateur à l'aide d'un jeton valide.
     * @param token Le jeton de réinitialisation.
     * @param newPassword Le nouveau mot de passe.
     */
    public void resetPassword(String token, String newPassword) {
        log.debug("Tentative de réinitialisation de mot de passe avec le jeton: {}", token);
        com.tradeall.tradefood.entity.PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.error("Jeton de réinitialisation invalide: {}", token);
                    return new RuntimeException("Invalid token");
                });
        
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.error("Jeton de réinitialisation expiré pour l'utilisateur: {}", resetToken.getUser().getEmail());
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
        log.info("Mot de passe réinitialisé avec succès pour l'utilisateur: {}", user.getEmail());
    }

    /**
     * Enregistre un nouvel utilisateur et le synchronise avec Sellsy en tant que contact.
     * @param request Les informations d'inscription.
     * @return La réponse contenant le jeton JWT.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        log.debug("Inscription d'un nouvel utilisateur: {}", request.getEmail());
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.ROLE_USER)
                .build();
        
        log.debug("Création du contact Sellsy pour: {} {}", request.getFirstName(), request.getLastName());
        // Création du client dans Sellsy
        com.tradeall.tradefood.dto.sellsy.SellsyContact sellsyContact = new com.tradeall.tradefood.dto.sellsy.SellsyContact();
        sellsyContact.setFirst_name(request.getFirstName());
        sellsyContact.setLast_name(request.getLastName());
        sellsyContact.setEmail(request.getEmail());
        
        try {
            com.tradeall.tradefood.dto.sellsy.SellsyContact createdContact = sellsyClient.createContact(sellsyContact).block();
            if (createdContact != null && createdContact.getId() != null) {
                log.info("Contact Sellsy créé avec ID: {}, récupération des détails...", createdContact.getId());
                // Récupération via GET comme demandé
                com.tradeall.tradefood.dto.sellsy.SellsyContact fetchedContact = sellsyClient.getContact(createdContact.getId()).block();
                if (fetchedContact != null) {
                    log.info("Contact Sellsy récupéré avec succès ID: {}", fetchedContact.getId());
                    user.setSellsyId(fetchedContact.getId());
                    user.setSellsyContactId(fetchedContact.getId().toString());
                    // Synchronisation de champs supplémentaires si présents
                    if (fetchedContact.getCreated() != null) user.setCreated(fetchedContact.getCreated());
                    if (fetchedContact.getUpdated() != null) user.setUpdated(fetchedContact.getUpdated());
                }
            } else {
                log.warn("Le contact Sellsy n'a pas pu être créé (réponse nulle ou ID manquant)");
            }
        } catch (Exception e) {
            log.error("Erreur lors de la création/récupération du contact Sellsy pour {}: {}", request.getEmail(), e.getMessage());
            // On continue l'inscription locale même si Sellsy échoue
        }
        
        userRepository.save(user);
        log.info("Utilisateur enregistré localement avec succès: {}", user.getEmail());
        
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Authentifie un utilisateur existant.
     * @param request Les informations de connexion.
     * @return La réponse contenant le jeton JWT.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.debug("Tentative d'authentification pour: {}", request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("Utilisateur non trouvé après authentification: {}", request.getEmail());
                    return new RuntimeException("User not found");
                });
        
        log.info("Utilisateur authentifié avec succès: {}", request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
