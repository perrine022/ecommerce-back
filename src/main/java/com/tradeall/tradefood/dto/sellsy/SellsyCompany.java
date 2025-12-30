package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyCompany {
    private Long id;
    private String type;
    private String name;
    private String email;
    private String website;
    private String phone_number;
    private String mobile_number;
    private String fax_number;
    private String reference;
    private String note;
    private String created;
    private String updated_at;
    private Boolean is_archived;
    private SellsyLegalFrance legal_france;
    private SellsySocial social;
    private Long main_contact_id;
    private Long invoicing_address_id;
    private Long delivery_address_id;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyLegalFrance {
        private String siret;
        private String siren;
        private String vat;
        private String ape_naf_code;
        private String company_type;
        private String rcs_immatriculation;

        public String getSiret() { return siret; }
        public void setSiret(String siret) { this.siret = siret; }
        public String getSiren() { return siren; }
        public void setSiren(String siren) { this.siren = siren; }
        public String getVat() { return vat; }
        public void setVat(String vat) { this.vat = vat; }
        public String getApe_naf_code() { return ape_naf_code; }
        public void setApe_naf_code(String ape_naf_code) { this.ape_naf_code = ape_naf_code; }
        public String getCompany_type() { return company_type; }
        public void setCompany_type(String company_type) { this.company_type = company_type; }
        public String getRcs_immatriculation() { return rcs_immatriculation; }
        public void setRcs_immatriculation(String rcs_immatriculation) { this.rcs_immatriculation = rcs_immatriculation; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsySocial {
        private String twitter;
        private String facebook;
        private String linkedin;
        private String viadeo;

        public String getTwitter() { return twitter; }
        public void setTwitter(String twitter) { this.twitter = twitter; }
        public String getFacebook() { return facebook; }
        public void setFacebook(String facebook) { this.facebook = facebook; }
        public String getLinkedin() { return linkedin; }
        public void setLinkedin(String linkedin) { this.linkedin = linkedin; }
        public String getViadeo() { return viadeo; }
        public void setViadeo(String viadeo) { this.viadeo = viadeo; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getPhone_number() { return phone_number; }
    public void setPhone_number(String phone_number) { this.phone_number = phone_number; }
    public String getMobile_number() { return mobile_number; }
    public void setMobile_number(String mobile_number) { this.mobile_number = mobile_number; }
    public String getFax_number() { return fax_number; }
    public void setFax_number(String fax_number) { this.fax_number = fax_number; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }
    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
    public Boolean getIs_archived() { return is_archived; }
    public void setIs_archived(Boolean is_archived) { this.is_archived = is_archived; }
    public SellsyLegalFrance getLegal_france() { return legal_france; }
    public void setLegal_france(SellsyLegalFrance legal_france) { this.legal_france = legal_france; }
    public SellsySocial getSocial() { return social; }
    public void setSocial(SellsySocial social) { this.social = social; }
    public Long getMain_contact_id() { return main_contact_id; }
    public void setMain_contact_id(Long main_contact_id) { this.main_contact_id = main_contact_id; }
    public Long getInvoicing_address_id() { return invoicing_address_id; }
    public void setInvoicing_address_id(Long invoicing_address_id) { this.invoicing_address_id = invoicing_address_id; }
    public Long getDelivery_address_id() { return delivery_address_id; }
    public void setDelivery_address_id(Long delivery_address_id) { this.delivery_address_id = delivery_address_id; }
}
