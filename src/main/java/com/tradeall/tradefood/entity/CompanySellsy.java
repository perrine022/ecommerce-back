package com.tradeall.tradefood.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "companies_sellsy")
public class CompanySellsy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sellsy_id", unique = true)
    private Long sellsyId;

    private String type;
    private String name;
    private String email;
    private String website;
    private String phoneNumber;
    private String mobileNumber;
    private String faxNumber;
    private String reference;
    
    @Column(columnDefinition = "TEXT")
    private String note;
    
    private String created;
    private String updated;
    private Boolean isArchived;
    
    // Legal France
    private String siret;
    private String siren;
    private String vat;
    private String apeNafCode;
    private String companyType;
    private String rcsImmatriculation;
    
    // Social
    private String twitter;
    private String facebook;
    private String linkedin;
    private String viadeo;
    
    private Long mainContactId;
    private Long invoicingAddressId;
    private Long deliveryAddressId;

    // Owner
    private Long ownerId;
    private String ownerType;

    public CompanySellsy() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Long getSellsyId() { return sellsyId; }
    public void setSellsyId(Long sellsyId) { this.sellsyId = sellsyId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
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
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }
    public String getUpdated() { return updated; }
    public void setUpdated(String updated) { this.updated = updated; }
    public Boolean getIsArchived() { return isArchived; }
    public void setIsArchived(Boolean isArchived) { this.isArchived = isArchived; }
    public String getSiret() { return siret; }
    public void setSiret(String siret) { this.siret = siret; }
    public String getSiren() { return siren; }
    public void setSiren(String siren) { this.siren = siren; }
    public String getVat() { return vat; }
    public void setVat(String vat) { this.vat = vat; }
    public String getApeNafCode() { return apeNafCode; }
    public void setApeNafCode(String apeNafCode) { this.apeNafCode = apeNafCode; }
    public String getCompanyType() { return companyType; }
    public void setCompanyType(String companyType) { this.companyType = companyType; }
    public String getRcsImmatriculation() { return rcsImmatriculation; }
    public void setRcsImmatriculation(String rcsImmatriculation) { this.rcsImmatriculation = rcsImmatriculation; }
    public String getTwitter() { return twitter; }
    public void setTwitter(String twitter) { this.twitter = twitter; }
    public String getFacebook() { return facebook; }
    public void setFacebook(String facebook) { this.facebook = facebook; }
    public String getLinkedin() { return linkedin; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }
    public String getViadeo() { return viadeo; }
    public void setViadeo(String viadeo) { this.viadeo = viadeo; }
    public Long getMainContactId() { return mainContactId; }
    public void setMainContactId(Long mainContactId) { this.mainContactId = mainContactId; }
    public Long getInvoicingAddressId() { return invoicingAddressId; }
    public void setInvoicingAddressId(Long invoicingAddressId) { this.invoicingAddressId = invoicingAddressId; }
    public Long getDeliveryAddressId() { return deliveryAddressId; }
    public void setDeliveryAddressId(Long deliveryAddressId) { this.deliveryAddressId = deliveryAddressId; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getOwnerType() { return ownerType; }
    public void setOwnerType(String ownerType) { this.ownerType = ownerType; }
}