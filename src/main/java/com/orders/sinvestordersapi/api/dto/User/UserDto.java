package com.orders.sinvestordersapi.api.dto.User;

import javax.validation.constraints.Email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    @Email
    private String username;

}
