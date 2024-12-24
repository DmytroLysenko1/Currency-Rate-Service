package com.example.service.impl;

import com.example.dto.crypto.CryptoRateResponseDTO;
import com.example.entity.CryptoRates;
import com.example.exception.DataFetchException;
import com.example.exception.InvalidRateException;
import com.example.repository.CryptoRatesRepository;
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


public class CryptoRateServiceImplTest {
    @Mock
    private CryptoRatesRepository cryptoRatesRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CryptoRateServiceImpl cryptoRateService;

    @BeforeEach
    void setUp() throws Exception {
        try (AutoCloseable openMocks = MockitoAnnotations.openMocks(this)) {
            when(this.webClient.get()).thenReturn(this.requestHeadersUriSpec);
            when(this.requestHeadersUriSpec.uri(any(String.class))).thenReturn(this.requestHeadersUriSpec);
            when(this.requestHeadersUriSpec.retrieve()).thenReturn(this.responseSpec);
            when(this.responseSpec.onStatus(any(), any())).thenReturn(this.responseSpec);
        }
    }


    @Test
    void getCryptoCurrencyRates_validResponse_success() {
        CryptoRates cryptoRate = getCryptoRatesBTC();
        when(this.responseSpec.bodyToFlux(CryptoRates.class)).thenReturn(Flux.just(cryptoRate));
        when(this.cryptoRatesRepository.save(any(CryptoRates.class))).thenReturn(Mono.just(cryptoRate));

        Mono<List<CryptoRates>> result = this.cryptoRateService.getCryptoCurrencyRates();

        StepVerifier.create(result)
                .expectNextMatches(rates -> rates.size() == 1 && "BTC".equals(rates.get(0).getCurrency()))
                .verifyComplete();
    }

    @Test
    void getCryptoCurrencyRates_errorResponse_error() {
        when(this.responseSpec.bodyToFlux(CryptoRates.class))
                .thenReturn(Flux.error(new DataFetchException("Error")));

        Mono<List<CryptoRates>> result = this.cryptoRateService.getCryptoCurrencyRates();

        StepVerifier.create(result)
                .expectError(DataFetchException.class)
                .verify();
    }

    @Test
    void getCryptoCurrencyRates_emptyResponse_emptyList() {
        when(this.responseSpec.bodyToFlux(CryptoRates.class)).thenReturn(Flux.empty());

        Mono<List<CryptoRates>> result = this.cryptoRateService.getCryptoCurrencyRates();

        StepVerifier.create(result)
                .expectNextMatches(List::isEmpty)
                .verifyComplete();
    }

    @Test
    void getCryptoCurrencyRates_partialSuccess_partialList() {
        CryptoRates cryptoRate1 = getCryptoRatesBTC();
        CryptoRates cryptoRate2 = getCryptoRatesETH();
        when(this.responseSpec.bodyToFlux(CryptoRates.class)).thenReturn(Flux.just(cryptoRate1, cryptoRate2));
        when(this.cryptoRatesRepository.save(cryptoRate1)).thenReturn(Mono.just(cryptoRate1));
        when(this.cryptoRatesRepository.save(cryptoRate2)).thenReturn(Mono.error(new RuntimeException("Save failed")));
        when(this.responseSpec.bodyToFlux(CryptoRates.class)).thenReturn(Flux.just(cryptoRate1, cryptoRate2));
        when(this.cryptoRatesRepository.save(cryptoRate1)).thenReturn(Mono.just(cryptoRate1));
        when(this.cryptoRatesRepository.save(cryptoRate2)).thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mono<List<CryptoRates>> result = this.cryptoRateService.getCryptoCurrencyRates();

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }


    @Test
    void getCryptoCurrencyRates_nullResponse_emptyList() {
        when(this.responseSpec.bodyToFlux(CryptoRates.class)).thenReturn(Flux.empty());

        Mono<List<CryptoRates>> result = this.cryptoRateService.getCryptoCurrencyRates();

        StepVerifier.create(result)
                .expectNextMatches(List::isEmpty)
                .verifyComplete();
    }

    @Test
    void getCryptoCurrencyRates_multipleValidResponses_success() {
        CryptoRates cryptoRate1 = getCryptoRatesBTC();
        CryptoRates cryptoRate2 = getCryptoRatesETH();
        when(this.responseSpec.bodyToFlux(CryptoRates.class)).thenReturn(Flux.just(cryptoRate1, cryptoRate2));
        when(this.cryptoRatesRepository.save(cryptoRate1)).thenReturn(Mono.just(cryptoRate1));
        when(this.cryptoRatesRepository.save(cryptoRate2)).thenReturn(Mono.just(cryptoRate2));

        Mono<List<CryptoRates>> result = this.cryptoRateService.getCryptoCurrencyRates();

        StepVerifier.create(result)
                .expectNextMatches(rates -> rates.size() == 2 &&
                        "BTC".equals(rates.get(0).getCurrency()) &&
                        "ETH".equals(rates.get(1).getCurrency()))
                .verifyComplete();
    }

    @Test
    void getCryptoCurrencyRates_mixedValidAndErrorResponses_partialList() {
        CryptoRates cryptoRate1 = getCryptoRatesBTC();
        CryptoRates cryptoRate2 = getCryptoRatesETH();
        when(this.responseSpec.bodyToFlux(CryptoRates.class)).thenReturn(Flux.just(cryptoRate1, cryptoRate2));
        when(this.cryptoRatesRepository.save(cryptoRate1)).thenReturn(Mono.just(cryptoRate1));
        when(this.cryptoRatesRepository.save(cryptoRate2)).thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mono<List<CryptoRates>> result = this.cryptoRateService.getCryptoCurrencyRates();

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_validInput_success() {
        CryptoRateResponseDTO dto = getCryptoRateResponseDTOWithBTCData();
        CryptoRates entity = getCryptoRatesBTCWithData();

        when(this.cryptoRatesRepository.save(any(CryptoRates.class))).thenReturn(Mono.just(entity));

        Mono<CryptoRateResponseDTO> result = this.cryptoRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectNextMatches(response -> "BTC".equals(response.getCurrency())
                        && new BigDecimal("50000.0").compareTo(response.getRate()) == 0)
                .verifyComplete();
    }

    @Test
    void saveCurrencyRates_nullInput_throwsInvalidRateException() {
        Mono<CryptoRateResponseDTO> result = this.cryptoRateService.saveCurrencyRates(null);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_nullCurrencyOrRate_throwsInvalidRateException() {
        CryptoRateResponseDTO dto = getCryptoRateResponseDTOWithNullCurrency();
        Mono<CryptoRateResponseDTO> result = this.cryptoRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_emptyCurrency_throwsInvalidRateException() {
        CryptoRateResponseDTO dto = getCryptoRateResponseDTOWithEmptyCurrency();

        when(this.cryptoRatesRepository.save(any(CryptoRates.class))).thenReturn(Mono.error(new InvalidRateException("Currency cannot be empty")));

        Mono<CryptoRateResponseDTO> result = this.cryptoRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_negativeRate_throwsInvalidRateException() {
        CryptoRateResponseDTO dto = getCryptoRateResponseDTOWithNegativeRate();

        when(this.cryptoRatesRepository.save(any(CryptoRates.class))).thenReturn(Mono.error(new InvalidRateException("Rate cannot be negative")));

        Mono<CryptoRateResponseDTO> result = this.cryptoRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_zeroRate_throwsInvalidRateException() {
        CryptoRateResponseDTO dto = getCryptoRateResponseDTOWithZeroRate();

        when(this.cryptoRatesRepository.save(any(CryptoRates.class))).thenReturn(Mono.error(new InvalidRateException("Rate cannot be zero")));

        Mono<CryptoRateResponseDTO> result = this.cryptoRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectError(InvalidRateException.class)
                .verify();
    }

    @Test
    void saveCurrencyRates_validInputDifferentCurrency_success() {
        CryptoRateResponseDTO dto = getCryptoRateResponseDTOWithETHData();
        CryptoRates entity = getCryptoRatesETH();

        when(this.cryptoRatesRepository.save(any(CryptoRates.class))).thenReturn(Mono.just(entity));

        Mono<CryptoRateResponseDTO> result = this.cryptoRateService.saveCurrencyRates(dto);

        StepVerifier.create(result)
                .expectNextMatches(response -> "ETH".equals(response.getCurrency()) && new BigDecimal("50000.0").compareTo(response.getRate()) == 0)
                .verifyComplete();
    }
}
