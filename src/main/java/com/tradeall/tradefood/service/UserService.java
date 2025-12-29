package com.tradeall.tradefood.service;

import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

/**
 * Service gérant la gestion des utilisateurs (profil, administration).
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Récupère la liste de tous les utilisateurs inscrits.
     * @return Liste des utilisateurs.
     */
    public List<User> getAllUsers() {
        log.debug("Récupération de tous les utilisateurs");
        return userRepository.findAll();
    }

    /**
     * Récupère un utilisateur par son identifiant unique.
     * @param id L'identifiant de l'utilisateur.
     * @return L'utilisateur trouvé.
     */
    public User getUserById(UUID id) {
        log.debug("Récupération de l'utilisateur ID: {}", id);
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("Utilisateur non trouvé ID: {}", id);
            return new RuntimeException("User not found");
        });
    }

    /**
     * Met à jour les informations de base d'un utilisateur.
     * @param id L'identifiant de l'utilisateur.
     * @param userDetails Les nouveaux détails.
     * @return L'utilisateur mis à jour.
     */
    public User updateUser(UUID id, User userDetails) {
        log.debug("Mise à jour de l'utilisateur ID: {}", id);
        User user = getUserById(id);
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        // Ne pas mettre à jour l'email ou le mot de passe ici par sécurité sans logique dédiée
        return userRepository.save(user);
    }

    /**
     * Supprime un utilisateur.
     * @param id L'identifiant de l'utilisateur.
     */
    public void deleteUser(UUID id) {
        log.debug("Suppression de l'utilisateur ID: {}", id);
        userRepository.deleteById(id);
    }
    
    /**
     * Récupère le profil d'un utilisateur via son email.
     * @param email L'adresse email de l'utilisateur.
     * @return L'utilisateur trouvé.
     */
    public User getProfile(String email) {
        log.debug("Récupération du profil pour l'email: {}", email);
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("Profil non trouvé pour l'email: {}", email);
            return new RuntimeException("User not found");
        });
    }
}
