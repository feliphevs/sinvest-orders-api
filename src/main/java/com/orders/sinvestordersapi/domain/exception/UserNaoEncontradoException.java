package com.orders.sinvestordersapi.domain.exception;

public class UserNaoEncontradoException extends EntidadeNaoEncontradaException {

    public UserNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public UserNaoEncontradoException(Long userId) {
        this(String.format("Não existe um usuário com código %d", userId));
    }
}