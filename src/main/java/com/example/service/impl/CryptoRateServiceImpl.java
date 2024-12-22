package com.example.service.impl;

import com.example.dto.crypto.CryptoRateResponseDTO;
import com.example.entity.CryptoRates;
import com.example.repository.CryptoRatesRepository;
import com.example.service.CryptoRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class CryptoRateServiceImpl implements CryptoRateService {
    private final WebClient cryptoWebClient;
    private final CryptoRatesRepository cryptoRateRepository;
    private static final String CRYPTO_URL = "/crypto-currency-rates";

    @Override
    public Mono<CryptoRateResponseDTO> getCryptoCurrencyRates() {
        return cryptoWebClient.get()
                .uri(CRYPTO_URL)
                .retrieve()
                .bodyToMono(CryptoRateResponseDTO.class)
                .flatMap(this::saveCurrencyRates);
    }

    @Override
    public Mono<CryptoRateResponseDTO> saveCurrencyRates(CryptoRateResponseDTO cryptoRateResponseDTO) {
        return Flux.fromIterable(cryptoRateResponseDTO.getRates())
                .flatMap(cryptoRate -> {
                    CryptoRates entity = new CryptoRates();
                    entity.setName(cryptoRate.getName());
                    entity.setValue(cryptoRate.getValue());
                    return cryptoRateRepository.save(entity);
                }).then(Mono.just(cryptoRateResponseDTO));
    }
}