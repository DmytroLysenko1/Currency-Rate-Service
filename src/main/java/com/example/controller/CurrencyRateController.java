package com.example.controller;


import com.example.service.CurrencyRateService;
import com.example.util.HttpStatusDescription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Map;


@Slf4j
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
        log.info("Received request to get all currency rates");
        return this.currencyRateService.getAllCurrencyRates()
                .doOnSuccess(rates -> log.info("Successfully retrieved currency rates"))
                .doOnError(error -> log.error("Error occurred while retrieving currency rates: {}", error.getMessage()));
    }
}
