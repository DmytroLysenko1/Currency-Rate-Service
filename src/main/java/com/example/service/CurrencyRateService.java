package com.example.service;

import com.example.entity.CryptoRates;
import com.example.entity.FiatRates;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CurrencyRateService {
    Mono<List<FiatRates>> getFiatCurrencyRates();
    Mono<List<CryptoRates>> getCryptoCurrencyRates();
}
