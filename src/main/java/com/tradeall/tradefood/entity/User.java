package com.tradeall.tradefood.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "sellsy_id")
    private Long sellsyId;

    @Column(name = "sellsy_contact_id")
    private String sellsyContactId;

    @Column(name = "civility")
    private String civility;

    @Column(name = "website")
    private String website;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "fax_number")
    private String faxNumber;

    @Column(name = "position")
    private String position;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;
    
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
    
    @Column(name = "invoicing_address_id")
    private Long invoicingAddressId;

    @Column(name = "delivery_address_id")
    private Long deliveryAddressId;
    
    // Social
    @Column(name = "twitter")
    private String twitter;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "linkedin")
    private String linkedin;

    @Column(name = "viadeo")
    private String viadeo;
    
    // Sync
    @Column(name = "sync_mailchimp")
    private Boolean syncMailchimp;

    @Column(name = "sync_mailjet")
    private Boolean syncMailjet;

    @Column(name = "sync_simplemail")
    private Boolean syncSimplemail;
    
    // Owner
    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "owner_type")
    private String ownerType;
    
    @Column(name = "created")
    private String created;

    @Column(name = "updated")
    private String updated;

    @Column(name = "is_archived")
    private Boolean isArchived;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_marketing_campaigns", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "campaign")
    private List<String> marketingCampaignsSubscriptions;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "sellsy_type")
    private String sellsyType;

    @Column(name = "company_name")
    private String companyName;

    public User() {}

    @Override
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getSellsyId() {
        return sellsyId;
    }

    public void setSellsyId(Long sellsyId) {
        this.sellsyId = sellsyId;
    }

    public String getSellsyContactId() {
        return sellsyContactId;
    }

    public void setSellsyContactId(String sellsyContactId) {
        this.sellsyContactId = sellsyContactId;
    }

    public String getCivility() { return civility; }
    public void setCivility(String civility) { this.civility = civility; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public String getFaxNumber() { return faxNumber; }
    public void setFaxNumber(String faxNumber) { this.faxNumber = faxNumber; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Long getInvoicingAddressId() { return invoicingAddressId; }
    public void setInvoicingAddressId(Long invoicingAddressId) { this.invoicingAddressId = invoicingAddressId; }
    public Long getDeliveryAddressId() { return deliveryAddressId; }
    public void setDeliveryAddressId(Long deliveryAddressId) { this.deliveryAddressId = deliveryAddressId; }
    public String getTwitter() { return twitter; }
    public void setTwitter(String twitter) { this.twitter = twitter; }
    public String getFacebook() { return facebook; }
    public void setFacebook(String facebook) { this.facebook = facebook; }
    public String getLinkedin() { return linkedin; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }
    public String getViadeo() { return viadeo; }
    public void setViadeo(String viadeo) { this.viadeo = viadeo; }
    public Boolean getSyncMailchimp() { return syncMailchimp; }
    public void setSyncMailchimp(Boolean syncMailchimp) { this.syncMailchimp = syncMailchimp; }
    public Boolean getSyncMailjet() { return syncMailjet; }
    public void setSyncMailjet(Boolean syncMailjet) { this.syncMailjet = syncMailjet; }
    public Boolean getSyncSimplemail() { return syncSimplemail; }
    public void setSyncSimplemail(Boolean syncSimplemail) { this.syncSimplemail = syncSimplemail; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getOwnerType() { return ownerType; }
    public void setOwnerType(String ownerType) { this.ownerType = ownerType; }
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }
    public String getUpdated() { return updated; }
    public void setUpdated(String updated) { this.updated = updated; }
    public Boolean getIsArchived() { return isArchived; }
    public void setIsArchived(Boolean isArchived) { this.isArchived = isArchived; }
    public List<String> getMarketingCampaignsSubscriptions() { return marketingCampaignsSubscriptions; }
    public void setMarketingCampaignsSubscriptions(List<String> marketingCampaignsSubscriptions) { this.marketingCampaignsSubscriptions = marketingCampaignsSubscriptions; }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSellsyType() {
        return sellsyType;
    }

    public void setSellsyType(String sellsyType) {
        this.sellsyType = sellsyType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private UUID id;
        private Long sellsyId;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private Role role;
        private String sellsyType;
        private String companyName;

        public UserBuilder id(UUID id) { this.id = id; return this; }
        public UserBuilder sellsyId(Long sellsyId) { this.sellsyId = sellsyId; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public UserBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public UserBuilder role(Role role) { this.role = role; return this; }
        public UserBuilder sellsyType(String sellsyType) { this.sellsyType = sellsyType; return this; }
        public UserBuilder companyName(String companyName) { this.companyName = companyName; return this; }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setSellsyId(sellsyId);
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(role);
            user.setSellsyType(sellsyType);
            user.setCompanyName(companyName);
            return user;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    }
}
