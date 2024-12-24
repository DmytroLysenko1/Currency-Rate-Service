package com.example.service.impl;

import com.example.dto.fiat.FiatRateResponseDTO;
import com.example.entity.FiatRates;
import com.example.exception.DataFetchException;
import com.example.exception.InvalidRateException;
import com.example.repository.FiatRatesRepository;
import com.example.service.FiatRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.example.util.ErrorMessage.FIAT_ERROR_MESSAGE;


@Service
@Slf4j
@RequiredArgsConstructor
public class FiatRateServiceImpl implements FiatRateService {
    private final WebClient webClient;
    private final FiatRatesRepository fiatRatesRepository;
    private static final String FIAT_URL = "/fiat-currency-rates";

    @Override
    public Mono<List<FiatRates>> getFiatCurrencyRates() {
        log.info("Fetching fiat currency rates from URL: {}", FIAT_URL);
        return this.webClient.get()
                .uri(FIAT_URL)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> {
                            log.error("Error fetching fiat currency rates: {}", response.statusCode());
                            return Mono.error(new DataFetchException(FIAT_ERROR_MESSAGE));
                        })
                .bodyToFlux(FiatRates.class)
                .switchIfEmpty(Flux.empty())
                .filter(fiatRates -> Objects.nonNull(fiatRates) && StringUtils.hasText(fiatRates.getCurrency()))
                .doOnError(error -> log.error("Error occurred while fetching fiat currency rates: {}", error.getMessage()))
                .flatMap(fiatRates -> {
                    log.info("Saving fetched fiat currency rate: {}", fiatRates);
                    return this.fiatRatesRepository.save(fiatRates)
                            .doOnError(e -> log.error("Error saving fiat currency rate: {}", e.getMessage()))
                            .thenReturn(fiatRates);
                })
                .collectList()
                .doOnSuccess(rates -> log.info("Successfully fetched and saved {} fiat currency rates", rates.size()))
                .onErrorResume(e -> {
                    log.error("Error during processing: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    @Transactional
    @Override
    public Mono<FiatRateResponseDTO> saveCurrencyRates(FiatRateResponseDTO fiatRateResponseDTO) {
        return Mono.justOrEmpty(fiatRateResponseDTO)
                .switchIfEmpty(Mono.error(new InvalidRateException("fiatRateResponseDTO must not be null")))
                .flatMap(dto -> {
                    if (dto.getCurrency() == null || dto.getCurrency().isEmpty() || dto.getRate() == null || dto.getRate().compareTo(BigDecimal.ZERO) <= 0) {
                        return Mono.error(new InvalidRateException("Currency must not be null or empty, and rate must be positive"));
                    }
                    FiatRates entity = new FiatRates();
                    entity.setCurrency(dto.getCurrency());
                    entity.setRate(dto.getRate());
                    return this.fiatRatesRepository.save(entity)
                            .doOnSuccess(savedEntity -> log.info("Successfully saved fiat currency rate: {}", savedEntity))
                            .thenReturn(dto);
                });
    }
}
