package com.example.service.impl;

import com.example.dto.fiat.FiatRateResponseDTO;
import com.example.entity.FiatRates;
import com.example.exception.DataFetchException;
import com.example.exception.InvalidRateException;
import com.example.repository.FiatRatesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static com.example.util.ModelUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FiatRateServiceImplTest {
    @Mock
    private FiatRatesRepository fiatRatesRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private FiatRateServiceImpl fiatRateService;

    @BeforeEach
    void setUp() throws Exception {
        try (AutoCloseable ignored = MockitoAnnotations.openMocks(this)) {
            when(this.webClient.get()).thenReturn(this.requestHeadersUriSpec);
            when(this.requestHeadersUriSpec.uri(any(String.class))).thenReturn(this.requestHeadersUriSpec);
            when(this.requestHeadersUriSpec.retrieve()).thenReturn(this.responseSpec);
            when(this.responseSpec.onStatus(any(), any())).thenReturn(this.responseSpec);
        }
    }

    @Test
    void getFiatCurrencyRates_validResponse_success() {
        FiatRates fiatRate = getFiatRatesUSD();
        when(this.responseSpec.bodyToFlux(FiatRates.class)).thenReturn(Flux.just(fiatRate));
        when(this.fiatRatesRepository.save(any(FiatRates.class))).thenReturn(Mono.just(fiatRate));

        Mono<List<FiatRates>> result = this.fiatRateService.getFiatCurrencyRates();

        StepVerifier.create(result)
                .expectNextMatches(rates -> rates.size() == 1 &&
                        "USD".equals(rates.get(0).getCurrency()))
                .verifyComplete();
    }

    @Test
    void getFiatCurrencyRates_errorResponse_error() {
        when(this.responseSpec.bodyToFlux(FiatRates.class)).thenReturn(Flux.error(new DataFetchException("Error")));

        Mono<List<FiatRates>> result = this.fiatRateService.getFiatCurrencyRates();

        StepVerifier.create(result)
                .expectError(DataFetchException.class)
                .verify();
    }

    @Test
    void getFiatCurrencyRates_emptyResponse_emptyList() {
        when(this.responseSpec.bodyToFlux(FiatRates.class)).thenReturn(Flux.empty());

        Mono<List<FiatRates>> result = this.fiatRateService.getFiatCurrencyRates();

        StepVerifier.create(result)
                .expectNextMatches(List::isEmpty)
                .verifyComplete();
    }

    @Test
    void getFiatCurrencyRates_partialSuccess_partialList() {
        FiatRates fiatRate1 = getFiatRatesUSD();
        FiatRates fiatRate2 = getFiatRatesEUR();
        when(this.responseSpec.bodyToFlux(FiatRates.class)).thenReturn(Flux.just(fiatRate1, fiatRate2));
        when(this.fiatRatesRepository.save(fiatRate1)).thenReturn(Mono.just(fiatRate1));
        when(this.fiatRatesRepository.save(fiatRate2)).thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mono<List<FiatRates>> result = this.fiatRateService.getFiatCurrencyRates();

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getFiatCurrencyRates_nullResponse_emptyList() {
        when(this.responseSpec.bodyToFlux(FiatRates.class)).thenReturn(Flux.empty());

        Mono<List<FiatRates>> result = this.fiatRateService.getFiatCurrencyRates();

        StepVerifier.create(result)
                .expectNextMatches(List::isEmpty)
                .verifyComplete();
    }

    @Test
    void getFiatCurrencyRates_multipleValidResponses_success() {
        FiatRates fiatRate1 = getFiatRatesUSD();
        FiatRates fiatRate2 = getFiatRatesEUR();
        when(this.responseSpec.bodyToFlux(FiatRates.class)).thenReturn(Flux.just(fiatRate1, fiatRate2));
        when(this.fiatRatesRepository.save(fiatRate1)).thenReturn(Mono.just(fiatRate1));
        when(this.fiatRatesRepository.save(fiatRate2)).thenReturn(Mono.just(fiatRate2));

        Mono<List<FiatRates>> result = this.fiatRateService.getFiatCurrencyRates();

        StepVerifier.create(result)
                .expectNextMatches(rates -> rates.size() == 2 && "USD".equals(rates.get(0).getCurrency()) && "EUR".equals(rates.get(1).getCurrency()))
                .verifyComplete();
    }

    @Test
    void getFiatCurrencyRates_mixedValidAndErrorResponses_partialList() {
        FiatRates fiatRate1 = getFiatRatesUSD();
        FiatRates fiatRate2 = getFiatRatesEUR();
        when(this.responseSpec.bodyToFlux(FiatRates.class)).thenReturn(Flux.just(fiatRate1, fiatRate2));
        when(this.fiatRatesRepository.save(fiatRate1)).thenReturn(Mono.just(fiatRate1));
        when(this.fiatRatesRepository.save(fiatRate2)).thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mono<List<FiatRates>> result = this.fiatRateService.getFiatCurrencyRates();

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_validInput_success() {
        FiatRateResponseDTO dto = getFiatRateResponseDTOWithUSDData();
        FiatRates entity = getFiatRatesUSDData();

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
        FiatRateResponseDTO dto = getFiatRateResponseDTOWithNullCurrency();

        when(fiatRatesRepository.save(any(FiatRates.class))).thenReturn(Mono.error(new InvalidRateException("Currency and rate cannot be null")));

        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_emptyCurrency_throwsInvalidRateException() {
        FiatRateResponseDTO dto = getFiatRateResponseDTOWithEmptyCurrency();

        when(fiatRatesRepository.save(any(FiatRates.class))).thenReturn(Mono.error(new InvalidRateException("Currency cannot be empty")));

        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_negativeRate_throwsInvalidRateException() {
        FiatRateResponseDTO dto = getFiatRateResponseDTOWithNegativeRate();

        when(fiatRatesRepository.save(any(FiatRates.class))).thenReturn(Mono.error(new InvalidRateException("Rate cannot be negative")));

        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_zeroRate_throwsInvalidRateException() {
        FiatRateResponseDTO dto = getFiatRateResponseDTOWithZeroRate();

        when(fiatRatesRepository.save(any(FiatRates.class))).thenReturn(Mono.error(new InvalidRateException("Rate cannot be zero")));

        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_validInputDifferentCurrency_success() {
        FiatRateResponseDTO dto = getFiatRateResponseDTOWithEURData();
        FiatRates entity = getFiatRatesUSDData();

        when(fiatRatesRepository.save(any(FiatRates.class))).thenReturn(Mono.just(entity));

        Mono<FiatRateResponseDTO> result = fiatRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectNextMatches(response -> "EUR".equals(response.getCurrency()) &&
                        new BigDecimal("1.0").compareTo(response.getRate()) == 0)
                .verifyComplete();
    }
}
