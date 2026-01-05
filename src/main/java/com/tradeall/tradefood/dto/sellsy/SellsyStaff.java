package com.tradeall.tradefood.dto.sellsy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellsyStaff {
    private Long id;
    private String lastname;
    private String firstname;
    private String email;
    private String avatar;
    private String status;
    private String position;
    private String phone_number;
    private String mobile_number;
    private String fax_number;
    private List<SellsyGroup> groups;
    private String civility;
    private String created;
    private Long profile;
    private SellsyTeam team;
    private SellsyJob job;
    private String timezone;
    private String language;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getPhone_number() { return phone_number; }
    public void setPhone_number(String phone_number) { this.phone_number = phone_number; }
    public String getMobile_number() { return mobile_number; }
    public void setMobile_number(String mobile_number) { this.mobile_number = mobile_number; }
    public String getFax_number() { return fax_number; }
    public void setFax_number(String fax_number) { this.fax_number = fax_number; }
    public List<SellsyGroup> getGroups() { return groups; }
    public void setGroups(List<SellsyGroup> groups) { this.groups = groups; }
    public String getCivility() { return civility; }
    public void setCivility(String civility) { this.civility = civility; }
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }
    public Long getProfile() { return profile; }
    public void setProfile(Long profile) { this.profile = profile; }
    public SellsyTeam getTeam() { return team; }
    public void setTeam(SellsyTeam team) { this.team = team; }
    public SellsyJob getJob() { return job; }
    public void setJob(SellsyJob job) { this.job = job; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyGroup {
        private Long id;
        private String name;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyTeam {
        private Long id;
        private String name;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellsyJob {
        private Long id;
        private String name;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
