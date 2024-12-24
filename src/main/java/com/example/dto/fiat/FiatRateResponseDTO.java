package com.example.dto.fiat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiatRateResponseDTO {
   private String currency;
   private BigDecimal rate;
}
