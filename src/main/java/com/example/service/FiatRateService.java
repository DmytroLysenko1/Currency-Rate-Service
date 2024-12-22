package com.example.service;

import com.example.dto.fiat.FiatRateResponseDTO;
import reactor.core.publisher.Mono;

public interface FiatRateService {
    Mono<FiatRateResponseDTO> saveCurrencyRates(FiatRateResponseDTO fiatRateResponseDTO);
}
