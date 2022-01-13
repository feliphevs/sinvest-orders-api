package com.orders.sinvestordersapi.domain.repository;

import com.orders.sinvestordersapi.domain.model.UserOrders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOrdersRepository extends JpaRepository<UserOrders, Long> {

}
