package com.example.blaze.test.domain.enums;

import java.math.BigDecimal;

public enum Taxes {
    CITY_TAX(BigDecimal.valueOf(0.1),"City Tax"),
    COUNTY_TAX(BigDecimal.valueOf(0.05), "County Tax"),
    STATE_TAX(BigDecimal.valueOf(0.08), "State Tax"),
    FEDERAL_TAX(BigDecimal.valueOf(0.02), "Federal Tax");

    private final BigDecimal rate;
    private final String name;

    public BigDecimal getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }

    Taxes(BigDecimal rate, String name) {
        this.name = name;
        this.rate = rate;
    }
}
