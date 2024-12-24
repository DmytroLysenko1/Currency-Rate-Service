package com.example.service;

import com.example.dto.crypto.CryptoRateResponseDTO;
import com.example.entity.CryptoRates;
import reactor.core.publisher.Mono;

import java.util.List;


public interface CryptoRateService {
    Mono<List<CryptoRates>> getCryptoCurrencyRates();
    Mono<CryptoRateResponseDTO> saveCurrencyRates(CryptoRateResponseDTO cryptoRateResponseDTO);
}
