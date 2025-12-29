package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyContact {
    private Long id;
    private String civility;
    private String first_name;
    private String last_name;
    private String email;
    private String website;
    private String phone_number;
    private String mobile_number;
    private String fax_number;
    private String position;
    private String birth_date;
    private String avatar;
    private String note;
    private Long invoicing_address_id;
    private Long delivery_address_id;
    private SellsySocial social;
    private SellsySync sync;
    private SellsyOwner owner;
    private String created;
    private String updated;
    private Boolean is_archived;
    private List<String> marketing_campaigns_subscriptions;
    private SellsyEmbed _embed;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyEmbed {
        private SellsyAddress invoicing_address;
        private SellsyAddress delivery_address;

        public SellsyAddress getInvoicing_address() { return invoicing_address; }
        public void setInvoicing_address(SellsyAddress invoicing_address) { this.invoicing_address = invoicing_address; }
        public SellsyAddress getDelivery_address() { return delivery_address; }
        public void setDelivery_address(SellsyAddress delivery_address) { this.delivery_address = delivery_address; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyAddress {
        private Long id;
        private String name;
        private String address_line_1;
        private String address_line_2;
        private String address_line_3;
        private String address_line_4;
        private String postal_code;
        private String city;
        private String country;
        private String country_code;
        private Boolean is_invoicing_address;
        private Boolean is_delivery_address;
        private SellsyGeocode geocode;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAddress_line_1() { return address_line_1; }
        public void setAddress_line_1(String address_line_1) { this.address_line_1 = address_line_1; }
        public String getAddress_line_2() { return address_line_2; }
        public void setAddress_line_2(String address_line_2) { this.address_line_2 = address_line_2; }
        public String getAddress_line_3() { return address_line_3; }
        public void setAddress_line_3(String address_line_3) { this.address_line_3 = address_line_3; }
        public String getAddress_line_4() { return address_line_4; }
        public void setAddress_line_4(String address_line_4) { this.address_line_4 = address_line_4; }
        public String getPostal_code() { return postal_code; }
        public void setPostal_code(String postal_code) { this.postal_code = postal_code; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getCountry_code() { return country_code; }
        public void setCountry_code(String country_code) { this.country_code = country_code; }
        public Boolean getIs_invoicing_address() { return is_invoicing_address; }
        public void setIs_invoicing_address(Boolean is_invoicing_address) { this.is_invoicing_address = is_invoicing_address; }
        public Boolean getIs_delivery_address() { return is_delivery_address; }
        public void setIs_delivery_address(Boolean is_delivery_address) { this.is_delivery_address = is_delivery_address; }
        public SellsyGeocode getGeocode() { return geocode; }
        public void setGeocode(SellsyGeocode geocode) { this.geocode = geocode; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyGeocode {
        private Double lat;
        private Double lng;

        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }
        public Double getLng() { return lng; }
        public void setLng(Double lng) { this.lng = lng; }
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyOwner {
        private Long id;
        private String type;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCivility() { return civility; }
    public void setCivility(String civility) { this.civility = civility; }
    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }
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
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getBirth_date() { return birth_date; }
    public void setBirth_date(String birth_date) { this.birth_date = birth_date; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Long getInvoicing_address_id() { return invoicing_address_id; }
    public void setInvoicing_address_id(Long invoicing_address_id) { this.invoicing_address_id = invoicing_address_id; }
    public Long getDelivery_address_id() { return delivery_address_id; }
    public void setDelivery_address_id(Long delivery_address_id) { this.delivery_address_id = delivery_address_id; }
    public SellsySocial getSocial() { return social; }
    public void setSocial(SellsySocial social) { this.social = social; }
    public SellsySync getSync() { return sync; }
    public void setSync(SellsySync sync) { this.sync = sync; }
    public SellsyOwner getOwner() { return owner; }
    public void setOwner(SellsyOwner owner) { this.owner = owner; }
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }
    public String getUpdated() { return updated; }
    public void setUpdated(String updated) { this.updated = updated; }
    public Boolean getIs_archived() { return is_archived; }
    public void setIs_archived(Boolean is_archived) { this.is_archived = is_archived; }
    public List<String> getMarketing_campaigns_subscriptions() { return marketing_campaigns_subscriptions; }
    public void setMarketing_campaigns_subscriptions(List<String> marketing_campaigns_subscriptions) { this.marketing_campaigns_subscriptions = marketing_campaigns_subscriptions; }
    public SellsyEmbed get_embed() { return _embed; }
    public void set_embed(SellsyEmbed _embed) { this._embed = _embed; }
}
