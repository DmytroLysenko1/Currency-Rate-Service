package com.example.service.impl;

import com.example.dto.fiat.FiatRateResponseDTO;
import com.example.entity.FiatRates;
import com.example.repository.FiatRatesRepository;
import com.example.service.FiatRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Service
@RequiredArgsConstructor
public class FiatRateServiceImpl implements FiatRateService {
    private final WebClient webClient;
    private final FiatRatesRepository fiatRatesRepository;
    private static final String FIAT_URL = "/fiat-currency-rates";

    @Override
    public Mono<FiatRateResponseDTO> getFiatCurrencyRates() {
        return webClient.get()
                .uri(FIAT_URL)
                .retrieve()
                .bodyToMono(FiatRateResponseDTO.class)
                .flatMap(this::saveCurrencyRates)
                .onErrorResume(e -> Mono.empty());
    }

    @Transactional
    @Override
    public Mono<FiatRateResponseDTO> saveCurrencyRates(FiatRateResponseDTO fiatRateResponseDTO) {
        return Flux.fromIterable(fiatRateResponseDTO.getRates())
                .flatMap(fiatRate -> {
                    FiatRates entity = new FiatRates();
                    entity.setCurrency(fiatRate.getCurrency());
                    entity.setRate(fiatRate.getRate());
                    return fiatRatesRepository.save(entity);
                }).then(Mono.just(fiatRateResponseDTO));
    }
}
