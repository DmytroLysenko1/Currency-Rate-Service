package com.example.controller;

import com.example.service.CurrencyRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class CurrencyRateControllerTest {

    @Mock
    private CurrencyRateService currencyRateService;

    @InjectMocks
    private CurrencyRateController currencyRateController;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        this.webTestClient = WebTestClient.bindToController(new CurrencyRateController(this.currencyRateService)).build();
    }

    @Test
    public void testGetAllCurrencyRatesSuccess() {
        Map<String, Object> mockResponse = Map.of(
                "fiatRates", List.of("USD", "EUR"),
                "cryptoRates", List.of("BTC", "ETH")
        );
        when(this.currencyRateService.getAllCurrencyRates()).thenReturn(Mono.just(mockResponse));

        this.webTestClient.get().uri("/currency-rates")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.fiatRates").isArray()
                .jsonPath("$.cryptoRates").isArray();
    }

    @Test
    public void testGetAllCurrencyRatesPartialSuccess() {
        Map<String, Object> mockPartialResponse = Map.of(
                "fiatRates", List.of("USD", "EUR"),
                "cryptoRates", List.of()
        );
        when(this.currencyRateService.getAllCurrencyRates()).thenReturn(Mono.just(mockPartialResponse));

        this.webTestClient.get().uri("/currency-rates")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.fiatRates").isArray()
                .jsonPath("$.cryptoRates").isArray()
                .jsonPath("$.cryptoRates.length()").isEqualTo(0);
    }
}
