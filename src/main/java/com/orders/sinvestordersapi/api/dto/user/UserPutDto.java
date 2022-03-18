package com.orders.sinvestordersapi.api.dto.user;

import java.math.BigDecimal;

import javax.validation.constraints.Email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPutDto {

    @Email
    private String username;
    private BigDecimal dollarBalance;
    private boolean enabled;
}
