package com.orders.sinvestordersapi.domain.service;

import com.orders.sinvestordersapi.domain.exception.UserNaoEncontradoException;
import com.orders.sinvestordersapi.domain.model.User;
import com.orders.sinvestordersapi.domain.repository.UserRepository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroUserService {

    private UserRepository userRepository;

    public User adicionar(User user) {
        return userRepository.save(user);
    }

    public void excluir(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNaoEncontradoException(userId);
        }
    }

    public User buscarOuFalhar(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNaoEncontradoException(userId));
    }
}
