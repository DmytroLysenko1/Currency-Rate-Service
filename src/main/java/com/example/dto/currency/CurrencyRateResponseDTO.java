package com.example.dto.currency;

import com.example.dto.crypto.CryptoRateResponseDTO;
import com.example.dto.fiat.FiatRateResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRateResponseDTO {
    private List<FiatRateResponseDTO> fiat;
    private List<CryptoRateResponseDTO> crypto;
}
