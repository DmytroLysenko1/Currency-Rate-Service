package com.example.service.impl;

import com.example.entity.CryptoRates;
import com.example.entity.FiatRates;
import com.example.service.CryptoRateService;
import com.example.service.CurrencyRateService;
import com.example.service.FiatRateService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {
    private final FiatRateService fiatRateService;
    private final CryptoRateService cryptoRateService;

    public Mono<Map<String, Object>> getAllCurrencyRates() {
        log.info("Fetching all currency rates");
        Mono<List<FiatRates>> fiatRates = this.fiatRateService.getFiatCurrencyRates();
        Mono<List<CryptoRates>> cryptoRates = this.cryptoRateService.getCryptoCurrencyRates();

        return getMapMono(fiatRates, cryptoRates);
    }

    @NotNull
    public static Mono<Map<String, Object>> getMapMono(Mono<List<FiatRates>> fiatRates, Mono<List<CryptoRates>> cryptoRates) {
        return Mono.zip(fiatRates, cryptoRates)
                .map(tuple -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("fiatRates", tuple.getT1());
                    response.put("cryptoRates", tuple.getT2());
                    log.info("Successfully fetched currency rates");
                    return response;
                });
    }
}
