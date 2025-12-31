package com.tradeall.tradefood.dto.sirene;

public class SireneCompanyInfoDTO {
    private String name;
    private String address;
    private String siret;

    public SireneCompanyInfoDTO() {}

    public SireneCompanyInfoDTO(String name, String address, String siret) {
        this.name = name;
        this.address = address;
        this.siret = siret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }
}
