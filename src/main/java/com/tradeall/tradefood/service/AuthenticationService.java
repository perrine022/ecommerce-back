package com.tradeall.tradefood.service;

import com.tradeall.tradefood.dto.auth.AuthenticationRequest;
import com.tradeall.tradefood.dto.auth.AuthenticationResponse;
import com.tradeall.tradefood.dto.auth.RegisterRequest;
import com.tradeall.tradefood.dto.sellsy.SellsyCompany;
import com.tradeall.tradefood.entity.ContactSellsy;
import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.repository.ContactSellsyRepository;
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
    private final ContactSellsyRepository contactSellsyRepository;
    private final com.tradeall.tradefood.repository.IndividualSellsyRepository individualSellsyRepository;
    private final com.tradeall.tradefood.repository.CompanySellsyRepository companySellsyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final com.tradeall.tradefood.client.SellsyClient sellsyClient;
    private final com.tradeall.tradefood.repository.PasswordResetTokenRepository tokenRepository;

    public AuthenticationService(UserRepository userRepository, 
                                 ContactSellsyRepository contactSellsyRepository,
                                 com.tradeall.tradefood.repository.IndividualSellsyRepository individualSellsyRepository,
                                 com.tradeall.tradefood.repository.CompanySellsyRepository companySellsyRepository,
                                 PasswordEncoder passwordEncoder, 
                                 JwtService jwtService, 
                                 AuthenticationManager authenticationManager, 
                                 com.tradeall.tradefood.client.SellsyClient sellsyClient, 
                                 com.tradeall.tradefood.repository.PasswordResetTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.contactSellsyRepository = contactSellsyRepository;
        this.individualSellsyRepository = individualSellsyRepository;
        this.companySellsyRepository = companySellsyRepository;
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
                .sellsyType("prospect")
                .companyName(request.getCompanyName())
                .siren(request.getSiren())
                .build();
        
        user.setPhoneNumber(request.getPhone());
        
        userRepository.save(user);
        log.info("Utilisateur enregistré localement avec succès: {}", user.getEmail());
        
        registerAsCompany(request, user);
        userRepository.save(user); // Save again with Sellsy info
        
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .sellsyId(user.getSellsyId())
                .build();
    }

    private void registerAsIndividual(RegisterRequest request, User user) {
        log.debug("Création de l'individu Sellsy pour: {} {}", request.getFirstName(), request.getLastName());
        com.tradeall.tradefood.dto.sellsy.SellsyIndividual sellsyIndividual = new com.tradeall.tradefood.dto.sellsy.SellsyIndividual();
        sellsyIndividual.setFirst_name(request.getFirstName());
        sellsyIndividual.setLast_name(request.getLastName());
        sellsyIndividual.setEmail(request.getEmail());
        sellsyIndividual.setType("prospect");

        try {
            com.tradeall.tradefood.dto.sellsy.SellsyIndividual createdIndividual = sellsyClient.createIndividual(sellsyIndividual).block();
            if (createdIndividual != null && createdIndividual.getId() != null) {
                log.info("Individu Sellsy créé avec ID: {}", createdIndividual.getId());
                user.setSellsyId(createdIndividual.getId());
                user.setSellsyContactId(createdIndividual.getId().toString());
                user.setCreated(createdIndividual.getCreated());
                user.setUpdated(createdIndividual.getUpdated_at());

                com.tradeall.tradefood.entity.IndividualSellsy individualEntity = new com.tradeall.tradefood.entity.IndividualSellsy();
                individualEntity.setSellsyId(createdIndividual.getId());
                individualEntity.setFirstName(createdIndividual.getFirst_name());
                individualEntity.setLastName(createdIndividual.getLast_name());
                individualEntity.setEmail(createdIndividual.getEmail());
                individualEntity.setType(createdIndividual.getType());
                individualEntity.setCreated(createdIndividual.getCreated());
                individualEntity.setUpdated(createdIndividual.getUpdated_at());
                individualSellsyRepository.save(individualEntity);
            }
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'individu Sellsy pour {}: {}", request.getEmail(), e.getMessage());
        }
    }

    private void registerAsCompany(RegisterRequest request, User user) {
        log.debug("Création de la compagnie Sellsy pour: {}", request.getCompanyName());
        com.tradeall.tradefood.dto.sellsy.SellsyCompanyRequest sellsyCompany = new com.tradeall.tradefood.dto.sellsy.SellsyCompanyRequest();
        
        // Initialisation de tous les champs String à vide par défaut
        sellsyCompany.setName(request.getCompanyName() != null ? request.getCompanyName() : "");
        sellsyCompany.setEmail(request.getEmail() != null ? request.getEmail() : "");
        sellsyCompany.setType("prospect");
        sellsyCompany.setWebsite("");
        sellsyCompany.setPhone_number(request.getPhone() != null ? request.getPhone() : "");
        sellsyCompany.setMobile_number("");
        sellsyCompany.setFax_number("");
        sellsyCompany.setReference("");
        sellsyCompany.setNote("");

        com.tradeall.tradefood.dto.sellsy.SellsyCompanyRequest.SellsySocialRequest social = new com.tradeall.tradefood.dto.sellsy.SellsyCompanyRequest.SellsySocialRequest();
        social.setFacebook("");
        social.setTwitter("");
        social.setLinkedin("");
        social.setViadeo("");
        sellsyCompany.setSocial(social);

        com.tradeall.tradefood.dto.sellsy.SellsyCompanyRequest.SellsyLegalFranceRequest legal = new com.tradeall.tradefood.dto.sellsy.SellsyCompanyRequest.SellsyLegalFranceRequest();
        String siren = request.getSiren() != null ? request.getSiren() : "";
        legal.setSiren(siren);
        legal.setSiret(!siren.isBlank() ? siren + "00000" : "");
        legal.setVat("FR99999999999");
        legal.setApe_naf_code("0000C");
        legal.setCompany_type("SAS");
        legal.setRcs_immatriculation("RCS xxxxx");
        sellsyCompany.setLegal_france(legal);

        // Autres champs
        sellsyCompany.setIs_archived(false);
        sellsyCompany.setCreated(java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")));

        try {
            log.info("Envoi de la requête de création de compagnie à Sellsy: {}", sellsyCompany.getName());
            com.tradeall.tradefood.dto.sellsy.SellsyCompany createdCompany = sellsyClient.createCompany(sellsyCompany).block();
            if (createdCompany != null && createdCompany.getId() != null) {
                log.info("Compagnie Sellsy créée avec ID: {}", createdCompany.getId());
                user.setSellsyId(createdCompany.getId());
                user.setSellsyContactId(createdCompany.getId().toString());
                user.setCreated(createdCompany.getCreated());
                user.setUpdated(createdCompany.getUpdated_at());

                com.tradeall.tradefood.entity.CompanySellsy companyEntity = new com.tradeall.tradefood.entity.CompanySellsy();
                companyEntity.setSellsyId(createdCompany.getId());
                companyEntity.setName(createdCompany.getName());
                companyEntity.setEmail(createdCompany.getEmail());
                companyEntity.setType(createdCompany.getType());
                companyEntity.setCreated(createdCompany.getCreated());
                companyEntity.setUpdated(createdCompany.getUpdated_at());
                companySellsyRepository.save(companyEntity);
            }
        } catch (Exception e) {
            log.error("Erreur lors de la création de la compagnie Sellsy pour {}: {}", request.getEmail(), e.getMessage());
        }
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

        if (!user.isEnabled()) {
            log.warn("Tentative de connexion d'un utilisateur non activé: {}", request.getEmail());
            throw new RuntimeException("Account is not active");
        }
        
        log.info("Utilisateur authentifié avec succès: {}", request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .sellsyId(user.getSellsyId())
                .build();
    }

    /**
     * Modifie le mot de passe d'un utilisateur connecté.
     */
    public void changePassword(User user, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Mot de passe modifié avec succès pour l'utilisateur: {}", user.getEmail());
    }
}
