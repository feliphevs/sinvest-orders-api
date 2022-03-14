package com.orders.sinvestordersapi.domain.exception;

public class StockInsuficienteException extends NegocioException {
    public StockInsuficienteException(String mensagem) {
        super(mensagem);
    }

    public StockInsuficienteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public StockInsuficienteException() {
        super("Não há stocks suficientes para realizar esta ordem de venda");
    }
}
