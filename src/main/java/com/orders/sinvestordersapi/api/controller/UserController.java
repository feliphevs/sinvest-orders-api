package com.orders.sinvestordersapi.api.controller;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orders.sinvestordersapi.api.dto.User.UserDto;
import com.orders.sinvestordersapi.api.dto.User.UserPutDto;
import com.orders.sinvestordersapi.api.dto.User.UserRespostaDto;
import com.orders.sinvestordersapi.core.ModelMapperUtils;
import com.orders.sinvestordersapi.domain.model.User;
import com.orders.sinvestordersapi.domain.repository.UserRepository;
import com.orders.sinvestordersapi.domain.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PatchMapping("/{userId}")
    public UserRespostaDto atualizarParcial(@PathVariable Long userId,
            @RequestBody Map<String, Object> campos, HttpServletRequest request) {
        User userAtual = userService.buscarOuFalhar(userId);

        merge(campos, userAtual, request);

        UserPutDto userConvertido = ModelMapperUtils.map(userAtual, UserPutDto.class);

        return atualizar(userId, userConvertido);
    }

    private void merge(Map<String, Object> dadosOrigem, User userDestino,
            HttpServletRequest request) {
        ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            User userOrigem = objectMapper.convertValue(dadosOrigem, User.class);

            dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
                Field field = ReflectionUtils.findField(User.class, nomePropriedade);
                field.setAccessible(true);

                Object novoValor = ReflectionUtils.getField(field, userOrigem);

                ReflectionUtils.setField(field, userDestino, novoValor);
            });
        } catch (IllegalArgumentException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
        }
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long userId) {
        userService.excluir(userId);
    }
}
