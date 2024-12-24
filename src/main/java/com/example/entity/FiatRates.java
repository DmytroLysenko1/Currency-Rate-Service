package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Table("fiat_rates")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class FiatRates {
    @Id
    @JsonIgnore
    private Long id;
    private String currency;
    private BigDecimal rate;
    @CreatedDate
    @JsonIgnore
    private LocalDateTime receivedAt;

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
