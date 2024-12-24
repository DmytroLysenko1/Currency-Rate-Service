package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static com.example.util.ApiKeys.API_HEADER;
import static com.example.util.ApiKeys.API_SECRET_KEY;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("http://localhost:8082")
                .defaultHeader(API_HEADER, API_SECRET_KEY)
                .filter((request, next) -> {
                    System.out.println("Request Headers: " + request.headers());
                    return next.exchange(request);
                })
                .build();
    }
}