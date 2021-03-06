package com.orders.sinvestordersapi.domain.exception;

public class UserOrderNaoEncontradaException extends NegocioException {

    public UserOrderNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public UserOrderNaoEncontradaException(Long userOrderId) {
        this(String.format("Não existe uma order com código %d", userOrderId));
    }
}
