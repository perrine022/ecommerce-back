package com.tradeall.tradefood.service;

import com.tradeall.tradefood.client.SellsyClient;
import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service gérant la gestion des utilisateurs (profil, administration).
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final SellsyClient sellsyClient;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, SellsyClient sellsyClient, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sellsyClient = sellsyClient;
        this.passwordEncoder = passwordEncoder;
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

    /**
     * Synchronise les contacts Sellsy vers la table des utilisateurs locaux.
     */
    @Transactional
    public void syncUsers() {
        log.info("Début de la synchronisation des clients depuis Sellsy");
        sellsyClient.getContacts(100, 0)
                .map(response -> {
                    log.debug("Reçu {} contacts de Sellsy", response.getData().size());
                    return response.getData().stream()
                            .map(contact -> {
                                User user = userRepository.findByEmail(contact.getEmail())
                                        .orElse(User.builder()
                                                .email(contact.getEmail())
                                                .password(passwordEncoder.encode("SellsyImport2025!"))
                                                .role(User.Role.ROLE_USER)
                                                .build());

                                user.setSellsyId(contact.getId());
                                user.setSellsyContactId(contact.getId().toString());
                                user.setFirstName(contact.getFirst_name());
                                user.setLastName(contact.getLast_name());
                                user.setCivility(contact.getCivility());
                                user.setWebsite(contact.getWebsite());
                                user.setPhoneNumber(contact.getPhone_number());
                                user.setMobileNumber(contact.getMobile_number());
                                user.setFaxNumber(contact.getFax_number());
                                user.setPosition(contact.getPosition());
                                user.setBirthDate(contact.getBirth_date());
                                user.setAvatar(contact.getAvatar());
                                user.setNote(contact.getNote());
                                user.setInvoicingAddressId(contact.getInvoicing_address_id());
                                user.setDeliveryAddressId(contact.getDelivery_address_id());
                                
                                if (contact.getSocial() != null) {
                                    user.setTwitter(contact.getSocial().getTwitter());
                                    user.setFacebook(contact.getSocial().getFacebook());
                                    user.setLinkedin(contact.getSocial().getLinkedin());
                                    user.setViadeo(contact.getSocial().getViadeo());
                                }
                                
                                if (contact.getSync() != null) {
                                    user.setSyncMailchimp(contact.getSync().getMailchimp());
                                    user.setSyncMailjet(contact.getSync().getMailjet());
                                    user.setSyncSimplemail(contact.getSync().getSimplemail());
                                }
                                
                                if (contact.getOwner() != null) {
                                    user.setOwnerId(contact.getOwner().getId());
                                    user.setOwnerType(contact.getOwner().getType());
                                }
                                
                                user.setCreated(contact.getCreated());
                                user.setUpdated(contact.getUpdated());
                                user.setIsArchived(contact.getIs_archived());
                                user.setMarketingCampaignsSubscriptions(contact.getMarketing_campaigns_subscriptions());
                                
                                return user;
                            })
                            .collect(Collectors.toList());
                })
                .subscribe(
                        users -> {
                            userRepository.saveAll(users);
                            log.info("Synchronisation réussie: {} utilisateurs mis à jour/créés", users.size());
                        },
                        error -> {
                            log.error("Erreur lors de la synchronisation des clients Sellsy: {}", error.getMessage());
                        }
                );
    }
}
