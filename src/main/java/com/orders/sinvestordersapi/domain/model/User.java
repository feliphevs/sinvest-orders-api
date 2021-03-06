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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
@Entity
public class User {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String username;

    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal dollarBalance = new BigDecimal(10000);

    @Column(nullable = false)
    private boolean enabled = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedOn;

    @Version
    private Long version;

    public void somaDollarBalance(BigDecimal valor) {
        this.dollarBalance = this.dollarBalance.add(valor);
    }

    public void subtraiDollarBalance(BigDecimal valor) {
        this.dollarBalance = this.dollarBalance.subtract(valor);
    }

}
