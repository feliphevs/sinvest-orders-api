package com.orders.sinvestordersapi.domain.service;

import com.orders.sinvestordersapi.domain.exception.UserStockBalanceNaoEncontradaException;
import com.orders.sinvestordersapi.domain.model.UserStockBalance;
import com.orders.sinvestordersapi.domain.model.UserStockBalancePKId;
import com.orders.sinvestordersapi.domain.repository.UserStockRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStockService {

    @Autowired
    private UserStockRepository userStockRepository;

    public UserStockBalance adicionar(UserStockBalance userStockBalance) {
        return userStockRepository.save(userStockBalance);
    }

    public UserStockBalance buscarOuFalhar(UserStockBalancePKId userStockId) {
        return userStockRepository.findById(userStockId)
                .orElseThrow(UserStockBalanceNaoEncontradaException::new);
    }

    public boolean existsById(UserStockBalancePKId userStockId) {
        return userStockRepository.existsById(userStockId);
    }

}
