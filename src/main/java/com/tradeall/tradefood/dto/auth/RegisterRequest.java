package com.tradeall.tradefood.dto.auth;


/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String companyName;
    private String siret;
    private String vatNumber;
    private String rcs;
    private String legalForm;
    private String phone;

    public RegisterRequest() {}

    public RegisterRequest(String firstName, String lastName, String email, String password, String companyName, String siret, String vatNumber, String rcs, String legalForm, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.siret = siret;
        this.vatNumber = vatNumber;
        this.rcs = rcs;
        this.legalForm = legalForm;
        this.phone = phone;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getSiret() { return siret; }
    public void setSiret(String siret) { this.siret = siret; }
    public String getVatNumber() { return vatNumber; }
    public void setVatNumber(String vatNumber) { this.vatNumber = vatNumber; }
    public String getRcs() { return rcs; }
    public void setRcs(String rcs) { this.rcs = rcs; }
    public String getLegalForm() { return legalForm; }
    public void setLegalForm(String legalForm) { this.legalForm = legalForm; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
