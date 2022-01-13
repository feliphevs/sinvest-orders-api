package com.orders.sinvestordersapi.api.controller;

import java.util.List;

import com.orders.sinvestordersapi.domain.model.User;
import com.orders.sinvestordersapi.domain.repository.UserRepository;
import com.orders.sinvestordersapi.domain.service.CadastroUserService;

import org.springframework.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CadastroUserService cadastroUser;

    @GetMapping
    public List<User> listar() {
        return userRepository.findAll();
    }

    @GetMapping("/{userId}")
    public User buscar(@PathVariable Long userId) {
        return cadastroUser.buscarOuFalhar(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User adicionar(@RequestBody User user) {
        return cadastroUser.adicionar(user);
    }

    @PutMapping("/{userId}")
    public User atualizar(@PathVariable Long userId,
            @RequestBody User user) {
        User userAtual = cadastroUser.buscarOuFalhar(userId);

        BeanUtils.copyProperties(user, userAtual, "id");

        return cadastroUser.adicionar(userAtual);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long userId) {
        cadastroUser.excluir(userId);
    }
}
