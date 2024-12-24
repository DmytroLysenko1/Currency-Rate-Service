package com.example.service.impl;

import com.example.dto.crypto.CryptoRateResponseDTO;
import com.example.entity.CryptoRates;
import com.example.exception.DataFetchException;
import com.example.exception.InvalidRateException;
import com.example.repository.CryptoRatesRepository;
import com.example.service.CryptoRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static com.example.util.ErrorMessage.CRYPTO_ERROR_MESSAGE;


@Service
@Slf4j
@RequiredArgsConstructor
public class CryptoRateServiceImpl implements CryptoRateService {
    private final CryptoRatesRepository cryptoRateRepository;
    private final WebClient webClient;
    private static final String CRYPTO_URL = "/crypto-currency-rates";

    @Override
    public Mono<List<CryptoRates>> getCryptoCurrencyRates() {
        log.info("Fetching crypto currency rates from URL: {}", CRYPTO_URL);
        return this.webClient.get()
                .uri(CRYPTO_URL)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> {
                            log.error("Error fetching crypto currency rates: {}", response.statusCode());
                            return Mono.error(new DataFetchException(CRYPTO_ERROR_MESSAGE));
                        })
                .bodyToFlux(CryptoRates.class)
                .switchIfEmpty(Flux.empty())
                .filter(cryptoRates -> Objects.nonNull(cryptoRates) && StringUtils.hasText(cryptoRates.getCurrency()))
                .doOnError(error -> log.error("Error occurred while fetching crypto currency rates: {}", error.getMessage()))
                .flatMap(cryptoRates -> {
                    log.info("Saving fetched crypto currency rate: {}", cryptoRates);
                    return this.cryptoRateRepository.save(cryptoRates).thenReturn(cryptoRates);
                })
                .collectList()
                .doOnSuccess(rates -> log.info("Successfully fetched and saved {} crypto currency rates", rates.size()));
    }

    @Override
    @Transactional
    public Mono<CryptoRateResponseDTO> saveCurrencyRates(CryptoRateResponseDTO cryptoRateResponseDTO) {
        return Mono.justOrEmpty(cryptoRateResponseDTO)
                .switchIfEmpty(Mono.error(new InvalidRateException("cryptoRateResponseDTO must not be null")))
                .flatMap(dto -> {
                    if (dto.getCurrency() == null || dto.getRate() == null) {
                        return Mono.error(new InvalidRateException("Currency and rate must not be null"));
                    }
                    CryptoRates entity = new CryptoRates();
                    entity.setCurrency(dto.getCurrency());
                    entity.setRate(dto.getRate());
                    return this.cryptoRateRepository.save(entity)
                            .doOnSuccess(savedEntity -> log.info("Successfully saved crypto currency rate: {}", savedEntity))
                            .thenReturn(dto);
                });
    }
}