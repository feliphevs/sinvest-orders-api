package com.orders.sinvestordersapi.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserStockBalancesPKId implements Serializable {

    private Long id_user;
    private Long id_stock;

}