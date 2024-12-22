package com.example.service.impl;

import com.example.entity.CryptoRates;
import com.example.entity.FiatRates;
import com.example.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final WebClient webClient;

    @Override
    public Mono<List<FiatRates>> getFiatCurrencyRates() {
        return webClient.get()
                .uri("/fiat-currency-rates")
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> Mono.error(new RuntimeException("Error fetching fiat currency rates")))
                .bodyToFlux(FiatRates.class)
                .doOnError(error -> System.out.println("Error: " + error.getMessage())) // Log error
                .collectList();
    }

    @Override
    public Mono<List<CryptoRates>> getCryptoCurrencyRates() {
        return webClient.get()
                .uri("/crypto-currency-rates")
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> Mono.error(new RuntimeException("Error fetching crypto currency rates")))
                .bodyToFlux(CryptoRates.class)
                .doOnError(error -> System.out.println("Error: " + error.getMessage())) // Log error
                .collectList();
    }
}

