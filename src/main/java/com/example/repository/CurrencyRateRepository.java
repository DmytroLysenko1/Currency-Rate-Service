package com.example.repository;

import com.example.entity.CurrencyRate;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRateRepository extends R2dbcRepository<CurrencyRate, Long> {
}
