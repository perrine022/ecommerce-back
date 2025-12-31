package com.tradeall.tradefood.service;

import com.tradeall.tradefood.client.SellsyClient;
import com.tradeall.tradefood.dto.sellsy.SellsyResponse;
import com.tradeall.tradefood.entity.ContactSellsy;
import com.tradeall.tradefood.entity.CompanySellsy;
import com.tradeall.tradefood.entity.IndividualSellsy;
import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.repository.ContactSellsyRepository;
import com.tradeall.tradefood.repository.CompanySellsyRepository;
import com.tradeall.tradefood.repository.IndividualSellsyRepository;
import com.tradeall.tradefood.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
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
    private final ContactSellsyRepository contactRepository;
    private final IndividualSellsyRepository individualRepository;
    private final CompanySellsyRepository companyRepository;
    private final SellsyClient sellsyClient;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, 
                       ContactSellsyRepository contactRepository,
                       IndividualSellsyRepository individualRepository,
                       CompanySellsyRepository companyRepository,
                       SellsyClient sellsyClient, 
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.individualRepository = individualRepository;
        this.companyRepository = companyRepository;
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
        log.info("Début de la synchronisation globale depuis Sellsy (Contacts, Individuals, Companies)");
        syncContacts(0, 100);
        syncIndividuals(0, 100);
        syncCompanies(0, 100);
    }

    private void syncContacts() {
        syncContacts(0, 100);
    }

    private void syncIndividuals() {
        syncIndividuals(0, 100);
    }

    private void syncCompanies() {
        syncCompanies(0, 100);
    }

    private void syncContacts(int offset, int limit) {
        sellsyClient.getContacts(limit, offset)
                .map(response -> {
                    log.debug("Reçu {} contacts de Sellsy (offset: {})", response.getData().size(), offset);
                    List<ContactSellsy> entities = response.getData().stream()
                            .filter(dto -> dto.getEmail() != null && !dto.getEmail().isBlank())
                            .map(dto -> {
                                ContactSellsy contact = contactRepository.findBySellsyId(dto.getId())
                                        .orElse(new ContactSellsy());

                                contact.setSellsyId(dto.getId());
                                contact.setFirstName(dto.getFirst_name());
                                contact.setLastName(dto.getLast_name());
                                contact.setEmail(dto.getEmail());
                                contact.setCivility(dto.getCivility());
                                contact.setWebsite(dto.getWebsite());
                                contact.setPhoneNumber(dto.getPhone_number());
                                contact.setMobileNumber(dto.getMobile_number());
                                contact.setFaxNumber(dto.getFax_number());
                                contact.setPosition(dto.getPosition());
                                contact.setBirthDate(dto.getBirth_date());
                                contact.setAvatar(dto.getAvatar());
                                contact.setNote(dto.getNote());
                                contact.setInvoicingAddressId(dto.getInvoicing_address_id());
                                contact.setDeliveryAddressId(dto.getDelivery_address_id());

                                if (dto.getSocial() != null) {
                                    contact.setTwitter(dto.getSocial().getTwitter());
                                    contact.setFacebook(dto.getSocial().getFacebook());
                                    contact.setLinkedin(dto.getSocial().getLinkedin());
                                    contact.setViadeo(dto.getSocial().getViadeo());
                                }

                                if (dto.getSync() != null) {
                                    contact.setSyncMailchimp(dto.getSync().getMailchimp());
                                    contact.setSyncMailjet(dto.getSync().getMailjet());
                                    contact.setSyncSimplemail(dto.getSync().getSimplemail());
                                }

                                if (dto.getOwner() != null) {
                                    contact.setOwnerId(dto.getOwner().getId());
                                    contact.setOwnerType(dto.getOwner().getType());
                                }

                                contact.setCreated(dto.getCreated());
                                contact.setUpdated(dto.getUpdated());
                                contact.setIsArchived(dto.getIs_archived());
                                contact.setMarketingCampaignsSubscriptions(dto.getMarketing_campaigns_subscriptions());

                                // Création/Mise à jour de l'utilisateur associé
                                createOrUpdateUserFromContact(contact);

                                return contact;
                            })
                            .collect(Collectors.toList());
                    return new AbstractMap.SimpleEntry<>(response, entities);
                })
                .subscribe(
                        entry -> {
                            List<ContactSellsy> entities = (List<ContactSellsy>) entry.getValue();
                            contactRepository.saveAll(entities);
                            log.info("Synchronisation réussie: {} contacts mis à jour/créés", entities.size());
                            SellsyResponse<?> response = (SellsyResponse<?>) entry.getKey();
                            if (response.getPagination() != null && response.getPagination().getTotal() > offset + limit) {
                                syncContacts(offset + limit, limit);
                            }
                        },
                        error -> log.error("Erreur lors de la synchronisation des contacts Sellsy: {}", error.getMessage())
                );
    }

    private void syncIndividuals(int offset, int limit) {
        sellsyClient.getIndividuals(limit, offset)
                .map(response -> {
                    log.debug("Reçu {} individus de Sellsy (offset: {})", response.getData().size(), offset);
                    List<IndividualSellsy> entities = response.getData().stream()
                            .filter(dto -> dto.getEmail() != null && !dto.getEmail().isBlank())
                            .map(dto -> {
                                IndividualSellsy individual = individualRepository.findBySellsyId(dto.getId())
                                        .orElse(new IndividualSellsy());

                                individual.setSellsyId(dto.getId());
                                individual.setType(dto.getType());
                                individual.setFirstName(dto.getFirst_name());
                                individual.setLastName(dto.getLast_name());
                                individual.setEmail(dto.getEmail());
                                individual.setCivility(dto.getCivility());
                                individual.setWebsite(dto.getWebsite());
                                individual.setPhoneNumber(dto.getPhone_number());
                                individual.setMobileNumber(dto.getMobile_number());
                                individual.setFaxNumber(dto.getFax_number());
                                individual.setReference(dto.getReference());
                                individual.setNote(dto.getNote());
                                individual.setInvoicingAddressId(dto.getInvoicing_address_id());
                                individual.setDeliveryAddressId(dto.getDelivery_address_id());
                                individual.setCreated(dto.getCreated());
                                individual.setUpdated(dto.getUpdated_at());
                                individual.setIsArchived(dto.getIs_archived());

                                if (dto.getSocial() != null) {
                                    individual.setTwitter(dto.getSocial().getTwitter());
                                    individual.setFacebook(dto.getSocial().getFacebook());
                                    individual.setLinkedin(dto.getSocial().getLinkedin());
                                    individual.setViadeo(dto.getSocial().getViadeo());
                                }

                                if (dto.getSync() != null) {
                                    individual.setSyncMailchimp(dto.getSync().getMailchimp());
                                    individual.setSyncMailjet(dto.getSync().getMailjet());
                                    individual.setSyncSimplemail(dto.getSync().getSimplemail());
                                }

                                // Création/Mise à jour de l'utilisateur associé
                                createOrUpdateUserFromIndividual(individual);

                                return individual;
                            })
                            .collect(Collectors.toList());
                    return new AbstractMap.SimpleEntry<>(response, entities);
                })
                .subscribe(
                        entry -> {
                            List<IndividualSellsy> entities = (List<IndividualSellsy>) entry.getValue();
                            individualRepository.saveAll(entities);
                            log.info("Synchronisation réussie: {} individus mis à jour/créés", entities.size());
                            SellsyResponse<?> response = (SellsyResponse<?>) entry.getKey();
                            if (response.getPagination() != null && response.getPagination().getTotal() > offset + limit) {
                                syncIndividuals(offset + limit, limit);
                            }
                        },
                        error -> log.error("Erreur lors de la synchronisation des individus Sellsy: {}", error.getMessage())
                );
    }

    private void syncCompanies(int offset, int limit) {
        sellsyClient.getCompanies(limit, offset)
                .map(response -> {
                    log.debug("Reçu {} compagnies de Sellsy (offset: {})", response.getData().size(), offset);
                    List<CompanySellsy> entities = response.getData().stream()
                            .map(dto -> {
                                CompanySellsy company = companyRepository.findBySellsyId(dto.getId())
                                        .orElse(new CompanySellsy());

                                company.setSellsyId(dto.getId());
                                company.setType(dto.getType());
                                company.setName(dto.getName());
                                company.setEmail(dto.getEmail());
                                company.setWebsite(dto.getWebsite());
                                company.setPhoneNumber(dto.getPhone_number());
                                company.setMobileNumber(dto.getMobile_number());
                                company.setFaxNumber(dto.getFax_number());
                                company.setReference(dto.getReference());
                                company.setNote(dto.getNote());
                                company.setCreated(dto.getCreated());
                                company.setUpdated(dto.getUpdated_at());
                                company.setIsArchived(dto.getIs_archived());
                                company.setMainContactId(dto.getMain_contact_id());
                                company.setInvoicingAddressId(dto.getInvoicing_address_id());
                                company.setDeliveryAddressId(dto.getDelivery_address_id());

                                if (dto.getLegal_france() != null) {
                                    company.setSiret(dto.getLegal_france().getSiret());
                                    company.setSiren(dto.getLegal_france().getSiren());
                                    company.setVat(dto.getLegal_france().getVat());
                                    company.setApeNafCode(dto.getLegal_france().getApe_naf_code());
                                    company.setCompanyType(dto.getLegal_france().getCompany_type());
                                    company.setRcsImmatriculation(dto.getLegal_france().getRcs_immatriculation());
                                }

                                if (dto.getSocial() != null) {
                                    company.setTwitter(dto.getSocial().getTwitter());
                                    company.setFacebook(dto.getSocial().getFacebook());
                                    company.setLinkedin(dto.getSocial().getLinkedin());
                                    company.setViadeo(dto.getSocial().getViadeo());
                                }

                                // Création/Mise à jour de l'utilisateur associé
                                createOrUpdateUserFromCompany(company);

                                return company;
                            })
                            .collect(Collectors.toList());
                    return new AbstractMap.SimpleEntry<>(response, entities);
                })
                .subscribe(
                        entry -> {
                            List<CompanySellsy> entities = (List<CompanySellsy>) entry.getValue();
                            companyRepository.saveAll(entities);
                            log.info("Synchronisation réussie: {} compagnies mises à jour/créées", entities.size());
                            SellsyResponse<?> response = (SellsyResponse<?>) entry.getKey();
                            if (response.getPagination() != null && response.getPagination().getTotal() > offset + limit) {
                                syncCompanies(offset + limit, limit);
                            }
                        },
                        error -> log.error("Erreur lors de la synchronisation des compagnies Sellsy: {}", error.getMessage())
                );
    }

    private void createOrUpdateUserFromContact(ContactSellsy contact) {
        if (contact.getEmail() == null || contact.getEmail().isBlank()) {
            return;
        }

        User user = userRepository.findByEmail(contact.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(contact.getEmail());
                    newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // Mot de passe temporaire aléatoire
                    newUser.setRole(User.Role.ROLE_USER);
                    return newUser;
                });

        user.setFirstName(contact.getFirstName());
        user.setLastName(contact.getLastName());
        user.setSellsyId(contact.getSellsyId());
        user.setSellsyType("contact");
        
        // Pour les contacts simples, on active par défaut s'ils sont synchronisés
        // ou on pourrait imaginer une logique liée à la compagnie parente.
        // Ici, on garde actif par défaut pour les contacts existants ou on peut rester prudent.
        user.setActive(true); 

        user.setCivility(contact.getCivility());
        user.setWebsite(contact.getWebsite());
        user.setPhoneNumber(contact.getPhoneNumber());
        user.setMobileNumber(contact.getMobileNumber());
        user.setFaxNumber(contact.getFaxNumber());
        user.setPosition(contact.getPosition());
        user.setBirthDate(contact.getBirthDate());
        user.setAvatar(contact.getAvatar());
        user.setNote(contact.getNote());
        user.setInvoicingAddressId(contact.getInvoicingAddressId());
        user.setDeliveryAddressId(contact.getDeliveryAddressId());
        user.setTwitter(contact.getTwitter());
        user.setFacebook(contact.getFacebook());
        user.setLinkedin(contact.getLinkedin());
        user.setViadeo(contact.getViadeo());
        user.setSyncMailchimp(contact.getSyncMailchimp());
        user.setSyncMailjet(contact.getSyncMailjet());
        user.setSyncSimplemail(contact.getSyncSimplemail());
        user.setOwnerId(contact.getOwnerId());
        user.setOwnerType(contact.getOwnerType());
        user.setCreated(contact.getCreated());
        user.setUpdated(contact.getUpdated());
        user.setIsArchived(contact.getIsArchived());
        user.setMarketingCampaignsSubscriptions(contact.getMarketingCampaignsSubscriptions());

        userRepository.save(user);
        log.debug("Utilisateur {} synchronisé depuis contact Sellsy ID {}", user.getEmail(), contact.getSellsyId());
    }

    private void createOrUpdateUserFromIndividual(IndividualSellsy individual) {
        if (individual.getEmail() == null || individual.getEmail().isBlank()) {
            return;
        }

        User user = userRepository.findByEmail(individual.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(individual.getEmail());
                    newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    newUser.setRole(User.Role.ROLE_USER);
                    return newUser;
                });

        user.setFirstName(individual.getFirstName());
        user.setLastName(individual.getLastName());
        user.setSellsyId(individual.getSellsyId());
        user.setSellsyType(individual.getType()); // Utiliser le type réel de Sellsy

        // Logique d'activation : client et non prospect
        if ("client".equalsIgnoreCase(individual.getType())) {
            user.setActive(true);
        } else {
            user.setActive(false);
        }

        user.setCivility(individual.getCivility());
        user.setWebsite(individual.getWebsite());
        user.setPhoneNumber(individual.getPhoneNumber());
        user.setMobileNumber(individual.getMobileNumber());
        user.setFaxNumber(individual.getFaxNumber());
        user.setBirthDate(null); // Non présent sur IndividualSellsy
        user.setNote(individual.getNote());
        user.setInvoicingAddressId(individual.getInvoicingAddressId());
        user.setDeliveryAddressId(individual.getDeliveryAddressId());
        user.setTwitter(individual.getTwitter());
        user.setFacebook(individual.getFacebook());
        user.setLinkedin(individual.getLinkedin());
        user.setViadeo(individual.getViadeo());
        user.setSyncMailchimp(individual.getSyncMailchimp());
        user.setSyncMailjet(individual.getSyncMailjet());
        user.setSyncSimplemail(individual.getSyncSimplemail());
        user.setCreated(individual.getCreated());
        user.setUpdated(individual.getUpdated());
        user.setIsArchived(individual.getIsArchived());

        userRepository.save(user);
        log.debug("Utilisateur {} synchronisé depuis individuel Sellsy ID {}", user.getEmail(), individual.getSellsyId());
    }

    private void createOrUpdateUserFromCompany(CompanySellsy company) {
        if (company.getEmail() == null || company.getEmail().isBlank()) {
            return;
        }

        User user = userRepository.findByEmail(company.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(company.getEmail());
                    newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    newUser.setRole(User.Role.ROLE_USER);
                    return newUser;
                });

        user.setFirstName(""); // Les compagnies n'ont pas de prénom
        user.setLastName(company.getName());
        user.setSellsyId(company.getSellsyId());
        user.setSellsyType(company.getType()); // Utiliser le type réel de Sellsy (prospect ou client)
        
        // Logique d'activation : client et non prospect
        if ("client".equalsIgnoreCase(company.getType())) {
            user.setActive(true);
        } else {
            user.setActive(false);
        }

        user.setWebsite(company.getWebsite());
        user.setPhoneNumber(company.getPhoneNumber());
        user.setMobileNumber(company.getMobileNumber());
        user.setFaxNumber(company.getFaxNumber());
        user.setNote(company.getNote());
        user.setInvoicingAddressId(company.getInvoicingAddressId());
        user.setDeliveryAddressId(company.getDeliveryAddressId());
        user.setTwitter(company.getTwitter());
        user.setFacebook(company.getFacebook());
        user.setLinkedin(company.getLinkedin());
        user.setViadeo(company.getViadeo());
        user.setCreated(company.getCreated());
        user.setUpdated(company.getUpdated());
        user.setIsArchived(company.getIsArchived());

        userRepository.save(user);
        log.debug("Utilisateur {} synchronisé depuis compagnie Sellsy ID {}", user.getEmail(), company.getSellsyId());
    }
}
