package com.orders.sinvestordersapi.domain.exception;

public class DollarInsuficienteException extends NegocioException {

    public DollarInsuficienteException(String mensagem) {
        super(mensagem);
    }

    public DollarInsuficienteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public DollarInsuficienteException() {
        this("Não há saldo suficiente para esta compra de stocks");
    }
}
