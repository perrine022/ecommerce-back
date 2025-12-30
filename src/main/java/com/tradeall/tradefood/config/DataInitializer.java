package com.tradeall.tradefood.config;

import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "perrine@gmail.com";
            if (userRepository.findByEmail(email).isEmpty()) {
                User perrine = User.builder()
                        .email(email)
                        .password(passwordEncoder.encode("Perrine"))
                        .firstName("Perrine")
                        .lastName("Honoré")
                        .role(User.Role.ROLE_ADMIN)
                        .build();

                perrine.setCivility("mrs");
                perrine.setWebsite("https://tradeall.com");
                perrine.setPhoneNumber("0123456789");
                perrine.setMobileNumber("0612345678");
                perrine.setFaxNumber("0123456780");
                perrine.setPosition("Directrice");
                perrine.setBirthDate("1990-01-01");
                perrine.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=Perrine");
                perrine.setNote("Compte administrateur par défaut");
                perrine.setSellsyId(12345L);
                perrine.setSellsyContactId("12345");
                perrine.setSellsyType("individual");
                perrine.setInvoicingAddressId(1L);
                perrine.setDeliveryAddressId(2L);
                perrine.setTwitter("perrine_h");
                perrine.setFacebook("perrine.honore");
                perrine.setLinkedin("perrine-honore");
                perrine.setViadeo("perrinehonore");
                perrine.setSyncMailchimp(true);
                perrine.setSyncMailjet(false);
                perrine.setSyncSimplemail(false);
                perrine.setOwnerId(1L);
                perrine.setOwnerType("staff");
                perrine.setIsArchived(false);
                perrine.setCreated("2023-10-01T10:00:00Z");
                perrine.setUpdated("2023-12-30T18:00:00Z");
                perrine.setMarketingCampaignsSubscriptions(new ArrayList<>());

                userRepository.save(perrine);
                System.out.println("Utilisateur par défaut créé : perrine@gmail.com / Perrine");
            }
        };
    }
}
