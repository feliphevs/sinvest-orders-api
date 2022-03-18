package com.orders.sinvestordersapi.api.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockDto {

    public StockDto(BigDecimal askMin, BigDecimal askMax, BigDecimal bidMin, BigDecimal bidMax) {
        this.askMin = askMin;
        this.askMax = askMax;
        this.bidMin = bidMin;
        this.bidMax = bidMax;
    }

    private BigDecimal askMin;
    private BigDecimal askMax;
    private BigDecimal bidMin;
    private BigDecimal bidMax;

}
