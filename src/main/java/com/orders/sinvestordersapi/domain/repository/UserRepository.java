package com.orders.sinvestordersapi.domain.repository;

import com.orders.sinvestordersapi.domain.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}