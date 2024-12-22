package com.example.service;

import com.example.dto.crypto.CryptoRateResponseDTO;
import reactor.core.publisher.Mono;

public interface CryptoRateService {
    Mono<CryptoRateResponseDTO> saveCurrencyRates(CryptoRateResponseDTO cryptoRateResponseDTO);
}
