package com.bok.parent.aggregator.dto;

import java.math.BigDecimal;

public class PriceResponseDTO {
    public String symbol;
    public BigDecimal price;

    public PriceResponseDTO(String symbol, BigDecimal price) {
        this.symbol = symbol;
        this.price = price;
    }

}
