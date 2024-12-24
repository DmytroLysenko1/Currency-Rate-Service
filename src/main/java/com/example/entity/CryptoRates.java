package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table("crypto_rates")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class CryptoRates {
    @Id
    @JsonIgnore
    private Long id;
    @JsonProperty("name")
    private String currency;
    @JsonProperty("value")
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
        CryptoRates that = (CryptoRates) o;
        return Objects.equals(currency, that.currency)
                && Objects.equals(rate, that.rate);
    }

    public void setRate(BigDecimal value) {
        this.rate = value.setScale(2, RoundingMode.DOWN);
    }
}
