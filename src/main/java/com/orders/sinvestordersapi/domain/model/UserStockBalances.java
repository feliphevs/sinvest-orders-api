package com.orders.sinvestordersapi.domain.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@IdClass(UserStockBalancesPKId.class)
public class UserStockBalances {

    @Id
    private Long id_user;

    @Id
    private Long id_stock;

    @Column(nullable = false)
    private String stock_symbol;

    @Column(nullable = false)
    private String stock_name;

    @Column(nullable = false)
    private Long volume;

    private Timestamp created_on;
    private Timestamp updated_on;
}
