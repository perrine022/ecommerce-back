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
    private String siren;
    private String phone;

    public RegisterRequest() {}

    public RegisterRequest(String firstName, String lastName, String email, String password, String companyName, String siren, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.siren = siren;
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
    public String getSiren() { return siren; }
    public void setSiren(String siren) { this.siren = siren; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
