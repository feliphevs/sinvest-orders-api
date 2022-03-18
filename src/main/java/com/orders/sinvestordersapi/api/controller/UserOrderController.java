package com.orders.sinvestordersapi.api.controller;

import java.util.List;

import javax.validation.Valid;

import com.orders.sinvestordersapi.api.dto.userorder.UserOrderDto;
import com.orders.sinvestordersapi.api.dto.userorder.UserOrderRespostaDto;
import com.orders.sinvestordersapi.core.ModelMapperUtils;
import com.orders.sinvestordersapi.domain.model.UserOrder;
import com.orders.sinvestordersapi.domain.repository.UserOrderRepository;
import com.orders.sinvestordersapi.domain.service.UserOrderService;

import org.springframework.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userorders")
public class UserOrderController {

    @Autowired
    private UserOrderRepository userOrderRepository;

    @Autowired
    private UserOrderService userOrderService;

    @GetMapping
    public List<UserOrderRespostaDto> listar() {
        return ModelMapperUtils.mapAll(userOrderRepository.findAllByOrderByIdAsc(), UserOrderRespostaDto.class);
    }

    @GetMapping("/{userOrderId}")
    public UserOrderRespostaDto buscar(@PathVariable Long userOrderId) {
        return ModelMapperUtils.map(userOrderService.buscarOuFalhar(userOrderId), UserOrderRespostaDto.class);
    }

    @GetMapping("/user/{idUser}")
    public List<UserOrderRespostaDto> listarOrdersUser(@PathVariable Long idUser) {

        return ModelMapperUtils.mapAll(userOrderRepository.listarPorIdUser(idUser), UserOrderRespostaDto.class);
    }

    @GetMapping("/compras/abertas")
    public List<UserOrderRespostaDto> listarComprasAbertas(
            @RequestParam(required = false, defaultValue = "0") String email) {
        return ModelMapperUtils.mapAll(userOrderRepository.comprasAbertasByUser(email),
                UserOrderRespostaDto.class);
    }

    @GetMapping("/compras/fechadas")
    public List<UserOrderRespostaDto> listarComprasFechadas(
            @RequestParam(required = false, defaultValue = "0") String email) {

        return ModelMapperUtils.mapAll(userOrderRepository.comprasFechadasByUser(email), UserOrderRespostaDto.class);
    }

    @GetMapping("/vendas/abertas")
    public List<UserOrderRespostaDto> listarVendasAbertas(
            @RequestParam(required = false, defaultValue = "0") String email) {

        return ModelMapperUtils.mapAll(userOrderRepository.vendasAbertasByUser(email), UserOrderRespostaDto.class);
    }

    @GetMapping("/vendas/fechadas")
    public List<UserOrderRespostaDto> listarVendasFechadas(
            @RequestParam(required = false, defaultValue = "0") String email) {

        return ModelMapperUtils.mapAll(userOrderRepository.vendasFechadasByUser(email), UserOrderRespostaDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserOrderRespostaDto adicionar(@RequestHeader("Authorization") String userToken,
            @RequestBody @Valid UserOrderDto userOrderDto) {

        UserOrder userOrder = ModelMapperUtils.map(userOrderDto, UserOrder.class);

        return ModelMapperUtils.map(userOrderService.adicionar(userOrder, userToken), UserOrderRespostaDto.class);
    }

    @PutMapping("/{userOrderId}")
    public UserOrderRespostaDto atualizar(@RequestHeader("Authorization") String userToken,
            @PathVariable Long userOrderId,
            @RequestBody UserOrderDto userOrderDto) {
        UserOrder userOrderAtual = userOrderService.buscarOuFalhar(userOrderId);

        BeanUtils.copyProperties(userOrderDto, userOrderAtual);

        return ModelMapperUtils.map(userOrderService.adicionar(userOrderAtual, userToken), UserOrderRespostaDto.class);
    }

    @PutMapping("/fechar/{userOrderId}")
    public UserOrderRespostaDto finalizaOrderById(@PathVariable Long userOrderId) {
        return ModelMapperUtils.map(userOrderService.finalizaOrderById(userOrderId), UserOrderRespostaDto.class);
    }

    @DeleteMapping("/{userOrderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long userOrderId) {
        userOrderService.excluir(userOrderId);
    }
}
