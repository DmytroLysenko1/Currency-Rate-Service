package com.example.repository;

import com.example.entity.FiatRates;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiatRatesRepository extends R2dbcRepository<FiatRates, Long> {
}
