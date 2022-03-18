package com.orders.sinvestordersapi.api.dto.userorder;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrderDto {

    private Long idUser;
    private Long idStock;
    private String stockSymbol;
    private String stockName;
    private Long volume;
    private Long volumeRemaining;
    private BigDecimal price;
    private int type;

}
