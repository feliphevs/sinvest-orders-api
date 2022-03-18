package com.orders.sinvestordersapi.domain.exception;

public class UserStockBalanceNaoEncontradaException extends NegocioException {

    public UserStockBalanceNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public UserStockBalanceNaoEncontradaException() {
        this("Não existe registro destes user e stock em userstockbalances");
    }
}
