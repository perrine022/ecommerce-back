package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyAddressDTO {
    private Long id;
    private String name = "";
    private String address_line_1 = "";
    private String address_line_2 = "";
    private String address_line_3 = "";
    private String address_line_4 = "";
    private String postal_code = "";
    private String city = "";
    private String country = "";
    private String country_code = "";
    private Boolean is_invoicing_address;
    private Boolean is_delivery_address;
    private Geocode geocode;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Geocode {
        private Double lat;
        private Double lng;

        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }
        public Double getLng() { return lng; }
        public void setLng(Double lng) { this.lng = lng; }
    }

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
    public Geocode getGeocode() { return geocode; }
    public void setGeocode(Geocode geocode) { this.geocode = geocode; }
}
