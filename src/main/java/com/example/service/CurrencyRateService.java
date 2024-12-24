package com.example.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface CurrencyRateService {
    Mono<Map<String, Object>> getAllCurrencyRates();
}
