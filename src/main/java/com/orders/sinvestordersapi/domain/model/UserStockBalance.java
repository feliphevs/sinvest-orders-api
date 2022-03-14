package com.orders.sinvestordersapi.domain.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "user_stock_balances")
@Entity
@IdClass(UserStockBalancePKId.class)
public class UserStockBalance {

    @Id
    private Long idUser;

    @Id
    private Long idStock;

    @Column(nullable = false, updatable = false)
    private String stockSymbol;

    @Column(nullable = false, updatable = false)
    private String stockName;

    @Column(nullable = false)
    private Long volume;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedOn;

    @Version
    private Long version;

    public void somaVolume(Long volume) {
        this.volume += volume;
    }

    public void subtraiVolume(Long volume) {
        this.volume -= volume;
    }
}
