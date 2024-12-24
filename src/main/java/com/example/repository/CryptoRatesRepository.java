package com.example.repository;

import com.example.entity.CryptoRates;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRatesRepository extends R2dbcRepository<CryptoRates, Long> {
}
