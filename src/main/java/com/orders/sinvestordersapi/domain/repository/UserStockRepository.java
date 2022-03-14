package com.orders.sinvestordersapi.domain.repository;

import java.util.List;

import com.orders.sinvestordersapi.domain.model.UserStockBalance;
import com.orders.sinvestordersapi.domain.model.UserStockBalancePKId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStockRepository extends JpaRepository<UserStockBalance, UserStockBalancePKId> {

    @Query("from UserStockBalance s inner join User u ON s.idUser = u.id AND u.username = :email")
    List<UserStockBalance> findStocksByUser(String email);

}
