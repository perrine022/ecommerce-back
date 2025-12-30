package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyIndividual {
    private Long id;
    private String type;
    private String last_name;
    private String first_name;
    private String civility;
    private String email;
    private String website;
    private String phone_number;
    private String mobile_number;
    private String fax_number;
    private String reference;
    private String note;
    private SellsySocial social;
    private Long invoicing_address_id;
    private Long delivery_address_id;
    private String created;
    private String updated_at;
    private Boolean is_archived;
    private SellsySync sync;

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsySync {
        private Boolean mailchimp;
        private Boolean mailjet;
        private Boolean simplemail;

        public Boolean getMailchimp() { return mailchimp; }
        public void setMailchimp(Boolean mailchimp) { this.mailchimp = mailchimp; }
        public Boolean getMailjet() { return mailjet; }
        public void setMailjet(Boolean mailjet) { this.mailjet = mailjet; }
        public Boolean getSimplemail() { return simplemail; }
        public void setSimplemail(Boolean simplemail) { this.simplemail = simplemail; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }
    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public String getCivility() { return civility; }
    public void setCivility(String civility) { this.civility = civility; }
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
    public SellsySocial getSocial() { return social; }
    public void setSocial(SellsySocial social) { this.social = social; }
    public Long getInvoicing_address_id() { return invoicing_address_id; }
    public void setInvoicing_address_id(Long invoicing_address_id) { this.invoicing_address_id = invoicing_address_id; }
    public Long getDelivery_address_id() { return delivery_address_id; }
    public void setDelivery_address_id(Long delivery_address_id) { this.delivery_address_id = delivery_address_id; }
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }
    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
    public Boolean getIs_archived() { return is_archived; }
    public void setIs_archived(Boolean is_archived) { this.is_archived = is_archived; }
    public SellsySync getSync() { return sync; }
    public void setSync(SellsySync sync) { this.sync = sync; }
}
