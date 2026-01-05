package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyCompany {
    private Long id;
    private String type = "";
    private String name = "";
    private String email = "";
    private String website = "";
    private String phone_number = "";
    private String mobile_number = "";
    private String fax_number = "";
    private String reference = "";
    private String note = "";
    private String created = "";
    private String updated_at = "";
    private Boolean is_archived;
    private SellsyLegalFrance legal_france;
    private SellsySocial social;
    private SellsyOwner owner;
    private Object business_segment;
    private Object number_of_employees;
    private SellsyEmbed _embed;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyOwner {
        private Long id;
        private String type;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyEmbed {
        private SellsyAddress invoicing_address;
        private SellsyAddress delivery_address;
        private SellsyContact main_contact;

        public SellsyAddress getInvoicing_address() { return invoicing_address; }
        public void setInvoicing_address(SellsyAddress invoicing_address) { this.invoicing_address = invoicing_address; }
        public SellsyAddress getDelivery_address() { return delivery_address; }
        public void setDelivery_address(SellsyAddress delivery_address) { this.delivery_address = delivery_address; }
        public SellsyContact getMain_contact() { return main_contact; }
        public void setMain_contact(SellsyContact main_contact) { this.main_contact = main_contact; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyAddress {
        private Long id;
        private String name = "";
        private String address_line_1 = "";
        private String postal_code = "";
        private String city = "";

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAddress_line_1() { return address_line_1; }
        public void setAddress_line_1(String address_line_1) { this.address_line_1 = address_line_1; }
        public String getPostal_code() { return postal_code; }
        public void setPostal_code(String postal_code) { this.postal_code = postal_code; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyLegalFrance {
        private String siret = "";
        private String siren = "";
        private String vat = "";
        private String ape_naf_code = "";
        private String company_type = "";
        private String rcs_immatriculation = "";

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
        private String twitter = "";
        private String facebook = "";
        private String linkedin = "";
        private String viadeo = "";

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
    public Object getBusiness_segment() { return business_segment; }
    public void setBusiness_segment(Object business_segment) { this.business_segment = business_segment; }
    public Object getNumber_of_employees() { return number_of_employees; }
    public void setNumber_of_employees(Object number_of_employees) { this.number_of_employees = number_of_employees; }
    public SellsyOwner getOwner() { return owner; }
    public void setOwner(SellsyOwner owner) { this.owner = owner; }
    public SellsyEmbed get_embed() { return _embed; }
    public void set_embed(SellsyEmbed _embed) { this._embed = _embed; }
}
