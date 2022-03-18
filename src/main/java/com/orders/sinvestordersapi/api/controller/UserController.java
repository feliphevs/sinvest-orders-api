package com.orders.sinvestordersapi.api.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import com.orders.sinvestordersapi.api.dto.user.UserDto;
import com.orders.sinvestordersapi.api.dto.user.UserPutDto;
import com.orders.sinvestordersapi.api.dto.user.UserRespostaDto;
import com.orders.sinvestordersapi.core.ModelMapperUtils;
import com.orders.sinvestordersapi.domain.model.User;
import com.orders.sinvestordersapi.domain.repository.UserRepository;
import com.orders.sinvestordersapi.domain.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserRespostaDto> listar() {
        return ModelMapperUtils.mapAll(userRepository.findAllByOrderByIdAsc(),
                UserRespostaDto.class);
    }

    @GetMapping("/{userId}")
    public UserRespostaDto buscar(@PathVariable Long userId) {
        return ModelMapperUtils.map(userService.buscarOuFalhar(userId), UserRespostaDto.class);
    }

    @GetMapping("/saldo")
    public BigDecimal buscarSaldo(@RequestParam(required = false, defaultValue = "0") String email) {
        User user = userRepository.findByUsername(email);
        return user.getDollarBalance();
    }

    @GetMapping("/userid")
    public Long buscarUserId(@RequestParam(required = false, defaultValue = "0") String email) {
        User user = userRepository.findByUsername(email);
        return user.getId();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRespostaDto adicionar(@RequestBody @Valid UserDto userDto) {
        User userSalvo = userService.adicionar(ModelMapperUtils.map(userDto, User.class));
        return ModelMapperUtils.map(userSalvo, UserRespostaDto.class);
    }

    @PutMapping("/{userId}")
    public UserRespostaDto atualizar(@PathVariable Long userId,
            @RequestBody @Valid UserPutDto userPutDto) {
        User userAtual = userService.buscarOuFalhar(userId);

        BeanUtils.copyProperties(userPutDto, userAtual);

        return ModelMapperUtils.map(userService.adicionar(userAtual), UserRespostaDto.class);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long userId) {
        userService.excluir(userId);
    }
}
