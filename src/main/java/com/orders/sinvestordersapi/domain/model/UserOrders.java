package com.orders.sinvestordersapi.domain.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class UserOrders {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public Long id_user;

    @Column(nullable = false)
    public Long id_stock;

    @Column(nullable = false)
    public String stock_symbol;

    @Column(nullable = false)
    public String stock_name;

    @Column(nullable = false)
    public Long volume;

    @Column(nullable = false)
    public BigDecimal price;

    @Column(nullable = false)
    public int type;

    @Column(nullable = false)
    public int status;

    public Timestamp created_on;
    public Timestamp updated_on;
}
