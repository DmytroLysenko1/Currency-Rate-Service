package com.example.controller;

import com.example.entity.CryptoRates;
import com.example.entity.FiatRates;
import com.example.service.CurrencyRateService;
import com.example.util.HttpStatusDescription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/currency-rates")
@RequiredArgsConstructor
public class CurrencyRateController {
    private final CurrencyRateService currencyRateService;

    @Operation(summary = "Get all currency rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatusDescription.OK),
            @ApiResponse(responseCode = "500", description = HttpStatusDescription.INTERNAL_SERVER_ERROR)
    })
    @GetMapping
    public Mono<Map<String, Object>> getAllCurrencyRates() {
        Mono<List<FiatRates>> fiatRates = currencyRateService.getFiatCurrencyRates();
        Mono<List<CryptoRates>> cryptoRates = currencyRateService.getCryptoCurrencyRates();

        return Mono.zip(fiatRates, cryptoRates)
                .map(tuple -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("fiatRates", tuple.getT1());
                    response.put("cryptoRates", tuple.getT2());
                    return response;
                });
    }
}
