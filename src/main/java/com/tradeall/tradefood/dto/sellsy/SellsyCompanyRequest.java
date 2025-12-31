package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SellsyCompanyRequest {
    private String type = "prospect";
    private String name = "";
    private String email = "";
    private String website = "";
    private String phone_number = "";
    private String mobile_number = "";
    private String fax_number = "";
    private SellsyLegalFranceRequest legal_france = new SellsyLegalFranceRequest();
    private String capital = "";
    private String reference = "";
    private String note = "";
    private String auxiliary_code = "";
    private SellsySocialRequest social = new SellsySocialRequest();
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = SafeIntegerDeserializer.class)
    private Integer rate_category_id;
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = SafeIntegerDeserializer.class)
    private Integer accounting_code_id;
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = SafeIntegerDeserializer.class)
    private Integer accounting_purchase_code_id;
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = SafeIntegerDeserializer.class)
    private Integer owner_id;
    private Boolean is_archived = false;
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = SafeIntegerDeserializer.class)
    private Integer business_segment;
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = SafeIntegerDeserializer.class)
    private Integer number_of_employees;
    private List<String> marketing_campaigns_subscriptions = new ArrayList<>();
    private String created = "";

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyLegalFranceRequest {
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
    public static class SellsySocialRequest {
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

    // Getters and Setters
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
    public SellsyLegalFranceRequest getLegal_france() { return legal_france; }
    public void setLegal_france(SellsyLegalFranceRequest legal_france) { this.legal_france = legal_france; }
    public String getCapital() { return capital; }
    public void setCapital(String capital) { this.capital = capital; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getAuxiliary_code() { return auxiliary_code; }
    public void setAuxiliary_code(String auxiliary_code) { this.auxiliary_code = auxiliary_code; }
    public SellsySocialRequest getSocial() { return social; }
    public void setSocial(SellsySocialRequest social) { this.social = social; }
    public Integer getRate_category_id() { return rate_category_id; }
    public void setRate_category_id(Integer rate_category_id) { this.rate_category_id = rate_category_id; }
    public Integer getAccounting_code_id() { return accounting_code_id; }
    public void setAccounting_code_id(Integer accounting_code_id) { this.accounting_code_id = accounting_code_id; }
    public Integer getAccounting_purchase_code_id() { return accounting_purchase_code_id; }
    public void setAccounting_purchase_code_id(Integer accounting_purchase_code_id) { this.accounting_purchase_code_id = accounting_purchase_code_id; }
    public Integer getOwner_id() { return owner_id; }
    public void setOwner_id(Integer owner_id) { this.owner_id = owner_id; }
    public Boolean getIs_archived() { return is_archived; }
    public void setIs_archived(Boolean is_archived) { this.is_archived = is_archived; }
    public Integer getBusiness_segment() { return business_segment; }
    public void setBusiness_segment(Integer business_segment) { this.business_segment = business_segment; }
    public Integer getNumber_of_employees() { return number_of_employees; }
    public void setNumber_of_employees(Integer number_of_employees) { this.number_of_employees = number_of_employees; }
    public List<String> getMarketing_campaigns_subscriptions() { return marketing_campaigns_subscriptions; }
    public void setMarketing_campaigns_subscriptions(List<String> marketing_campaigns_subscriptions) { this.marketing_campaigns_subscriptions = marketing_campaigns_subscriptions; }
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }
}
