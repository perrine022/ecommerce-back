package com.tradeall.tradefood.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean
    @org.springframework.context.annotation.Primary
    public WebClient webClient(WebClient.Builder builder, @Value("${sellsy.base-url:https://api.sellsy.com/v2}") String sellsyBaseUrl) {
        return createWebClient(builder, sellsyBaseUrl);
    }

    @Bean(name = "authWebClient")
    public WebClient authWebClient(WebClient.Builder builder, @Value("${sellsy.auth-url:https://login.sellsy.com/oauth2/access-tokens}") String authUrl) {
        return createWebClient(builder, authUrl);
    }

    private WebClient createWebClient(WebClient.Builder builder, String baseUrl) {
        ConnectionProvider provider = ConnectionProvider.builder("sellsy-pool")
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofMinutes(1))
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(30, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(30, TimeUnit.SECONDS)))
                .responseTimeout(Duration.ofSeconds(30));

        return builder
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
