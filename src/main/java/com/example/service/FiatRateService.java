package com.example.service;

import com.example.dto.fiat.FiatRateResponseDTO;
import com.example.entity.FiatRates;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FiatRateService {
    Mono<List<FiatRates>> getFiatCurrencyRates();
    Mono<FiatRateResponseDTO> saveCurrencyRates(FiatRateResponseDTO fiatRateResponseDTO);
}
