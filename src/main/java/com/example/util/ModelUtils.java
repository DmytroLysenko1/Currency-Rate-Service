package com.example.util;

import com.example.dto.crypto.CryptoRateResponseDTO;
import com.example.dto.fiat.FiatRateResponseDTO;
import com.example.entity.CryptoRates;
import com.example.entity.FiatRates;

import java.math.BigDecimal;

public class ModelUtils {
    public static CryptoRates getCryptoRatesBTC(){
        CryptoRates cryptoRates = new CryptoRates();
        cryptoRates.setCurrency("BTC");
        return cryptoRates;
    }
    public static CryptoRates getCryptoRatesETH(){
        CryptoRates cryptoRates = new CryptoRates();
        cryptoRates.setCurrency("ETH");
        return cryptoRates;
    }
    public static CryptoRates getCryptoRatesBTCWithData(){
        CryptoRates cryptoRates = new CryptoRates();
        cryptoRates.setCurrency("LTC");
        cryptoRates.setRate(new BigDecimal("50000.0"));
        return cryptoRates;
    }
    public static FiatRates getFiatRatesUSD(){
        FiatRates fiatRates = new FiatRates();
        fiatRates.setCurrency("USD");
        return fiatRates;
    }
    public static FiatRates getFiatRatesUSDData(){
        FiatRates fiatRates = new FiatRates();
        fiatRates.setCurrency("USD");
        fiatRates.setRate(new BigDecimal("1.0"));
        return fiatRates;
    }
    public static FiatRates getFiatRatesEUR(){
        FiatRates fiatRates = new FiatRates();
        fiatRates.setCurrency("EUR");
        return fiatRates;
    }
    public static CryptoRateResponseDTO getCryptoRateResponseDTOWithBTCData(){
        CryptoRateResponseDTO cryptoRateResponseDTO = new CryptoRateResponseDTO();
        cryptoRateResponseDTO.setCurrency("BTC");
        cryptoRateResponseDTO.setRate(new BigDecimal("50000.0"));
        return cryptoRateResponseDTO;
    }
    public static CryptoRateResponseDTO getCryptoRateResponseDTOWithETHData(){
        CryptoRateResponseDTO cryptoRateResponseDTO = new CryptoRateResponseDTO();
        cryptoRateResponseDTO.setCurrency("ETH");
        cryptoRateResponseDTO.setRate(new BigDecimal("50000.0"));
        return cryptoRateResponseDTO;
    }
    public static CryptoRateResponseDTO getCryptoRateResponseDTOWithNullCurrency(){
        CryptoRateResponseDTO cryptoRateResponseDTO = new CryptoRateResponseDTO();
        cryptoRateResponseDTO.setCurrency(null);
        cryptoRateResponseDTO.setRate(new BigDecimal("50000.0"));
        return cryptoRateResponseDTO;
    }
    public static CryptoRateResponseDTO getCryptoRateResponseDTOWithZeroRate(){
        CryptoRateResponseDTO cryptoRateResponseDTO = new CryptoRateResponseDTO();
        cryptoRateResponseDTO.setCurrency("BTC");
        cryptoRateResponseDTO.setRate(BigDecimal.ZERO);
        return cryptoRateResponseDTO;
    }
    public static CryptoRateResponseDTO getCryptoRateResponseDTOWithNegativeRate(){
        CryptoRateResponseDTO cryptoRateResponseDTO = new CryptoRateResponseDTO();
        cryptoRateResponseDTO.setCurrency("BTC");
        cryptoRateResponseDTO.setRate(new BigDecimal("-50000.0"));
        return cryptoRateResponseDTO;
    }
    public static CryptoRateResponseDTO getCryptoRateResponseDTOWithEmptyCurrency(){
        CryptoRateResponseDTO cryptoRateResponseDTO = new CryptoRateResponseDTO();
        cryptoRateResponseDTO.setCurrency("");
        cryptoRateResponseDTO.setRate(new BigDecimal("50000.0"));
        return cryptoRateResponseDTO;
    }
    public static FiatRateResponseDTO getFiatRateResponseDTOWithEURData(){
        FiatRateResponseDTO fiatRateResponseDTO = new FiatRateResponseDTO();
        fiatRateResponseDTO.setCurrency("EUR");
        fiatRateResponseDTO.setRate(new BigDecimal("1.0"));
        return fiatRateResponseDTO;
    }
    public static FiatRateResponseDTO getFiatRateResponseDTOWithUSDData(){
        FiatRateResponseDTO fiatRatesDto = new FiatRateResponseDTO();
        fiatRatesDto.setCurrency("USD");
        fiatRatesDto.setRate(new BigDecimal("1.0"));
        return fiatRatesDto;
    }
    public static FiatRateResponseDTO getFiatRateResponseDTOWithZeroRate(){
        FiatRateResponseDTO fiatRateResponseDTO = new FiatRateResponseDTO();
        fiatRateResponseDTO.setCurrency("USD");
        fiatRateResponseDTO.setRate(BigDecimal.ZERO);
        return fiatRateResponseDTO;
    }
    public static FiatRateResponseDTO getFiatRateResponseDTOWithNegativeRate(){
        FiatRateResponseDTO fiatRateResponseDTO = new FiatRateResponseDTO();
        fiatRateResponseDTO.setCurrency("USD");
        fiatRateResponseDTO.setRate(new BigDecimal("-1.0"));
        return fiatRateResponseDTO;
    }
    public static FiatRateResponseDTO getFiatRateResponseDTOWithEmptyCurrency(){
        FiatRateResponseDTO fiatRateResponseDTO = new FiatRateResponseDTO();
        fiatRateResponseDTO.setCurrency("");
        fiatRateResponseDTO.setRate(new BigDecimal("1.0"));
        return fiatRateResponseDTO;
    }
    public static FiatRateResponseDTO getFiatRateResponseDTOWithNullCurrency(){
        FiatRateResponseDTO fiatRateResponseDTO = new FiatRateResponseDTO();
        fiatRateResponseDTO.setCurrency(null);
        fiatRateResponseDTO.setRate(new BigDecimal("1.0"));
        return fiatRateResponseDTO;
    }

}
