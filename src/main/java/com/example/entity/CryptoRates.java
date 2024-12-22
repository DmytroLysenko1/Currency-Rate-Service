package com.example.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
@Setter
@Table("crypto_rates")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CryptoRates {
    private String name;
    private BigDecimal value;

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoRates that = (CryptoRates) o;
        return Objects.equals(name, that.name)
                && Objects.equals(value, that.value);
    }

    public void setValue(BigDecimal value) {
        this.value = value.setScale(2, RoundingMode.DOWN);
    }
}
