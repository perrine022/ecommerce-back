package com.tradeall.tradefood.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
@Configuration
public class SellsyConfig {

    @Value("${sellsy.base-url}")
    private String baseUrl;

    @Bean
    public WebClient sellsyWebClient(WebClient.Builder builder) {
        return builder.baseUrl(baseUrl).build();
    }
}
