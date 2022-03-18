package com.orders.sinvestordersapi.api.controller;

import java.util.List;

import com.orders.sinvestordersapi.api.dto.userstockbalance.UserStockDto;
import com.orders.sinvestordersapi.core.ModelMapperUtils;
import com.orders.sinvestordersapi.domain.model.UserStockBalance;
import com.orders.sinvestordersapi.domain.repository.UserStockRepository;
import com.orders.sinvestordersapi.domain.service.UserStockService;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userstockbalances")
public class UserStockBalanceController {

    @Autowired
    private UserStockRepository userStockRepository;

    @Autowired
    private UserStockService userStockService;

    @GetMapping
    public List<UserStockDto> listar() {
        return ModelMapperUtils.mapAll(userStockRepository.findAll(), UserStockDto.class);
    }

    @GetMapping("/user")
    public List<UserStockDto> listarStocksUser(@RequestParam(required = false, defaultValue = "0") String email) {
        return ModelMapperUtils.mapAll(userStockRepository.findStocksByUser(email), UserStockDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserStockDto adicionar(@RequestBody UserStockDto userStockDto) {
        UserStockBalance userStockSalva = userStockService
                .adicionar(ModelMapperUtils.map(userStockDto, UserStockBalance.class));
        return ModelMapperUtils.map(userStockSalva, UserStockDto.class);
    }

}
