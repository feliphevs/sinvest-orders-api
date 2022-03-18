package com.orders.sinvestordersapi.api.dto.userorder;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrderRespostaDto {

    private Long id;
    private Long idUser;
    private Long idStock;
    private String stockSymbol;
    private String stockName;
    private Long volume;
    private Long volumeRemaining;
    private BigDecimal price;
    private int type;
    private int status;
    private Date createdOn;
    private Date updatedOn;

}
