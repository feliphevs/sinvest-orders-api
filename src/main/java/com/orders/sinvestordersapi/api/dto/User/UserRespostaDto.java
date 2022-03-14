package com.orders.sinvestordersapi.api.dto.User;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRespostaDto {

    private Long id;
    private String username;
    private BigDecimal dollarBalance;
    private boolean enabled;

}
