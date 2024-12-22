package com.example.dto.crypto;

import com.example.entity.CryptoRates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoRateResponseDTO {
    private List<CryptoRates> rates;
}
