package com.example.repository;

import com.example.entity.CryptoRates;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRatesRepository extends ReactiveCrudRepository<CryptoRates, Long> {
}
