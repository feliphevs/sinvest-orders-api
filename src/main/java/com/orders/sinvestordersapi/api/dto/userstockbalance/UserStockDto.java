package com.orders.sinvestordersapi.api.dto.userstockbalance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStockDto {

    private Long idUser;
    private Long idStock;
    private String stockSymbol;
    private String stockName;
    private Long volume;

}
