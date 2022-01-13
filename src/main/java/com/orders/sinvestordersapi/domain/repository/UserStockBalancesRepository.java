package com.orders.sinvestordersapi.domain.repository;

import com.orders.sinvestordersapi.domain.model.UserStockBalances;
import com.orders.sinvestordersapi.domain.model.UserStockBalancesPKId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStockBalancesRepository extends JpaRepository<UserStockBalances, UserStockBalancesPKId> {

}
