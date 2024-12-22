package com.example.repository;

import com.example.entity.FiatRates;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiatRatesRepository extends ReactiveCrudRepository<FiatRates, Long> {
}
