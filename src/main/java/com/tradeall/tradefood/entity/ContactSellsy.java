package com.tradeall.tradefood.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "contacts_sellsy")
public class ContactSellsy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sellsy_id", unique = true)
    private Long sellsyId;

    private String civility;
    private String firstName;
    private String lastName;
    private String email;
    private String website;
    private String phoneNumber;
    private String mobileNumber;
    private String faxNumber;
    private String position;
    private String birthDate;
    
    @Column(columnDefinition = "TEXT")
    private String avatar;
    
    @Column(columnDefinition = "TEXT")
    private String note;
    
    private Long invoicingAddressId;
    private Long deliveryAddressId;
    
    // Social
    private String twitter;
    private String facebook;
    private String linkedin;
    private String viadeo;
    
    // Sync
    private Boolean syncMailchimp;
    private Boolean syncMailjet;
    private Boolean syncSimplemail;
    
    // Owner
    private Long ownerId;
    private String ownerType;
    
    private String created;
    private String updated;
    private Boolean isArchived;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "contact_sellsy_marketing_campaigns", joinColumns = @JoinColumn(name = "contact_id"))
    @Column(name = "campaign")
    private List<String> marketingCampaignsSubscriptions;

    public ContactSellsy() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Long getSellsyId() { return sellsyId; }
    public void setSellsyId(Long sellsyId) { this.sellsyId = sellsyId; }
    public String getCivility() { return civility; }
    public void setCivility(String civility) { this.civility = civility; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
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
}