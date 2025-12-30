package com.tradeall.tradefood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@SpringBootApplication
@EnableScheduling
public class TradefoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradefoodApplication.class, args);
    }

}
