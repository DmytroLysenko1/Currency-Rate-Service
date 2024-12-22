package com.example.dto.fiat;

import com.example.entity.FiatRates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiatRateResponseDTO {
   private List<FiatRates> rates;
}
