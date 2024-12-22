package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("http://localhost:8082")
                .defaultHeader("x-api-key", "secret-key")
                .filter((request, next) -> {
                    System.out.println("Request Headers: " + request.headers());
                    return next.exchange(request);
                })
                .build();
    }
}