package com.example.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.*;


import java.math.BigDecimal;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CurrencyRate {
    private String currency;
    private BigDecimal rate;
}
