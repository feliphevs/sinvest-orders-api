package com.orders.sinvestordersapi.domain.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "user_orders", schema = "public")
public class UserOrder {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idUser;

    @Column(nullable = false)
    private Long idStock;

    @Column(nullable = false)
    private String stockSymbol;

    @Column(nullable = false)
    private String stockName;

    @PositiveOrZero
    @Column(nullable = false)
    private Long volume;

    @PositiveOrZero
    @Column(nullable = false)
    private Long volumeRemaining;

    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int type;

    @Column(nullable = false)
    private int status = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedOn;

    @Version
    private Long version;

    public void somaVolumeRemaining(Long volumeRemaining) {
        this.volumeRemaining += volumeRemaining;
    }

    public void subtraiVolumeRemaining(Long volumeRemaining) {
        this.volumeRemaining -= volumeRemaining;
    }

    public void fechaOrder() {
        this.status = 1;
    }
}
