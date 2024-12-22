package com.example.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
@Setter
@Table("fiat_rates")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FiatRates extends CurrencyRate {
    private String currency;
    private BigDecimal rate;

    @Override
    public int hashCode() {
        return Objects.hash(currency, rate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FiatRates fiatRates = (FiatRates) o;
        return Objects.equals(currency, fiatRates.currency) &&
                Objects.equals(rate, fiatRates.rate);
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate.setScale(2, RoundingMode.DOWN);
    }
}
