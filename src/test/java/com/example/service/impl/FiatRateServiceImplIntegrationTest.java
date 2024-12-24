// src/test/java/com/example/service/impl/FiatRateServiceImplIntegrationTest.java
package com.example.service.impl;

import com.example.dto.fiat.FiatRateResponseDTO;
import com.example.entity.FiatRates;
import com.example.exception.InvalidRateException;
import com.example.repository.FiatRatesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
@AutoConfigureWebTestClient
public class FiatRateServiceImplIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private FiatRateServiceImpl fiatRateService;

    @Mock
    private FiatRatesRepository fiatRatesRepository;

    @Mock
    private WebClient webClient;

    @Test
    void saveCurrencyRates_validInput_success() {
        FiatRateResponseDTO dto = new FiatRateResponseDTO("USD", new BigDecimal("1.0"));
        FiatRates entity = new FiatRates();
        entity.setCurrency("USD");
        entity.setRate(new BigDecimal("1.0"));

        when(fiatRatesRepository.save(any(FiatRates.class))).thenReturn(Mono.just(entity));

        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectNextMatches(response -> "USD".equals(response.getCurrency()) && new BigDecimal("1.0").compareTo(response.getRate()) == 0)
                .verifyComplete();
    }

    @Test
    void saveCurrencyRates_nullInput_throwsInvalidRateException() {
        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(null);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_nullCurrencyOrRate_throwsInvalidRateException() {
        FiatRateResponseDTO dto = new FiatRateResponseDTO(null, new BigDecimal("1.0"));

        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_emptyCurrency_throwsInvalidRateException() {
        FiatRateResponseDTO dto = new FiatRateResponseDTO("", new BigDecimal("1.0"));

        when(fiatRatesRepository.save(any(FiatRates.class))).thenReturn(Mono.error(new InvalidRateException("Currency cannot be empty")));

        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_negativeRate_throwsInvalidRateException() {
        FiatRateResponseDTO dto = new FiatRateResponseDTO("USD", new BigDecimal("-1.0"));

        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof InvalidRateException &&
                        throwable.getMessage().equals("Currency must not be null or empty, and rate must be positive"))
                .verify();
    }

    @Test
    void saveCurrencyRates_zeroRate_throwsInvalidRateException() {
        FiatRateResponseDTO dto = new FiatRateResponseDTO("USD", BigDecimal.ZERO);

        when(fiatRatesRepository.save(any(FiatRates.class))).thenReturn(Mono.error(new InvalidRateException("Rate cannot be zero")));

        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_validInputDifferentCurrency_success() {
        FiatRateResponseDTO dto = new FiatRateResponseDTO("EUR", new BigDecimal("1.0"));
        FiatRates entity = new FiatRates();
        entity.setCurrency("EUR");
        entity.setRate(new BigDecimal("1.0"));

        when(fiatRatesRepository.save(any(FiatRates.class))).thenReturn(Mono.just(entity));

        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectNextMatches(response -> "EUR".equals(response.getCurrency()) && new BigDecimal("1.0").compareTo(response.getRate()) == 0)
                .verifyComplete();
    }
}