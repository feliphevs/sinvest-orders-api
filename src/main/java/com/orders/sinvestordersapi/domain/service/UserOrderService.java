package com.orders.sinvestordersapi.domain.service;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import com.orders.sinvestordersapi.api.dto.StockDto;
import com.orders.sinvestordersapi.domain.exception.DollarInsuficienteException;
import com.orders.sinvestordersapi.domain.exception.StockInsuficienteException;
import com.orders.sinvestordersapi.domain.exception.UserOrderNaoEncontradaException;
import com.orders.sinvestordersapi.domain.model.User;
import com.orders.sinvestordersapi.domain.model.UserOrder;
import com.orders.sinvestordersapi.domain.model.UserStockBalance;
import com.orders.sinvestordersapi.domain.model.UserStockBalancePKId;
import com.orders.sinvestordersapi.domain.repository.UserOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserOrderService {

    private static final String URL_STOCKS = "http://apistocks:8082/stocks/";

    @Autowired
    private UserOrderRepository userOrderRepository;

    @Autowired
    private UserStockService userStockService;

    @Autowired
    private UserService userService;

    public UserOrder adicionar(UserOrder userOrder, String userToken) {

        Long idUser = userOrder.getIdUser();
        Long idStock = userOrder.getIdStock();
        BigDecimal price = userOrder.getPrice();
        int type = userOrder.getType();
        Long volume = userOrder.getVolume();

        // fecha compra = 0
        // fecha venda = 1
        // fecha ambas = 2
        Integer fechaOrder;

        // compra = 0
        // venda = 1
        switch (type) {
            // compra
            case 0:
                if (!verificarSaldoDollar(idUser, volume, price)) {
                    throw new DollarInsuficienteException();
                }

                UserOrder orderCompra = userOrderRepository.save(userOrder);
                atualizaAskBidStockApi(URL_STOCKS, userToken, idStock);
                List<UserOrder> listaVendas = userOrderRepository.matchVenda(idUser, idStock, price);

                if (listaVendas != null) {

                    for (UserOrder venda : listaVendas) {
                        fechaOrder = transferencias(orderCompra, venda);

                        if (fechaOrder == 0 || fechaOrder == 2) {
                            orderCompra = buscarOuFalhar(orderCompra.getId());
                            return orderCompra;
                        }

                    }

                    orderCompra = buscarOuFalhar(orderCompra.getId());
                    return orderCompra;

                } else {
                    return orderCompra;
                }

                // venda
            case 1:
                UserStockBalancePKId userStockVendedorId = new UserStockBalancePKId(idUser, idStock);
                if (!verificarSaldoStock(userStockVendedorId, volume)) {
                    throw new StockInsuficienteException();
                }

                UserOrder orderVenda = userOrderRepository.save(userOrder);
                atualizaAskBidStockApi(URL_STOCKS, userToken, idStock);
                List<UserOrder> listaCompras = userOrderRepository.matchCompra(idUser, idStock, price);

                if (listaCompras != null) {

                    for (UserOrder compra : listaCompras) {
                        fechaOrder = transferencias(compra, orderVenda);

                        if (fechaOrder == 1 || fechaOrder == 2) {
                            orderVenda = buscarOuFalhar(orderVenda.getId());
                            return orderVenda;
                        }
                    }

                    orderVenda = buscarOuFalhar(orderVenda.getId());
                    return orderVenda;

                } else {
                    return orderVenda;
                }

            default:
                UserOrder userOrderSalva = userOrderRepository.save(userOrder);
                return userOrderSalva;
        }

    }

    public void excluir(Long userOrderId) {
        try {
            userOrderRepository.deleteById(userOrderId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserOrderNaoEncontradaException(userOrderId);
        }
    }

    public UserOrder buscarOuFalhar(Long userOrderId) {
        return userOrderRepository.findById(userOrderId)
                .orElseThrow(() -> new UserOrderNaoEncontradaException(userOrderId));
    }

    public UserOrder finalizaOrderById(Long userOrderId) {
        UserOrder order = buscarOuFalhar(userOrderId);
        order.fechaOrder();
        return userOrderRepository.save(order);
    }

    private boolean verificarSaldoDollar(Long idUser, Long volume, BigDecimal price) {
        User user = userService.buscarOuFalhar(idUser);

        BigDecimal dollarBallance = user.getDollarBalance();

        BigDecimal volumeStock = new BigDecimal(volume);
        BigDecimal totalCompra = volumeStock.multiply(price);

        BigDecimal dollarRestante = dollarBallance.subtract(totalCompra);

        return dollarRestante.compareTo(BigDecimal.ZERO) >= 0;
    }

    private boolean verificarSaldoStock(UserStockBalancePKId userStockId, Long volume) {
        UserStockBalance userStockVendedor = userStockService.buscarOuFalhar(userStockId);
        Long volumeStock = userStockVendedor.getVolume();

        return volume <= volumeStock;
    }

    private BigDecimal calcularPriceVolume(BigDecimal price, Long volume) {
        BigDecimal volumeStock = new BigDecimal(volume);
        return volumeStock.multiply(price);
    }

    private void atualizaAskBidStockApi(String urlStocks, String userToken, Long idStock) {

        URI uri = URI.create(urlStocks + idStock);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Authorization", userToken);

        BigDecimal askMin = userOrderRepository.askMin(idStock);
        BigDecimal askMax = userOrderRepository.askMax(idStock);
        BigDecimal bidMin = userOrderRepository.bidMin(idStock);
        BigDecimal bidMax = userOrderRepository.bidMax(idStock);

        StockDto stockModel = new StockDto(askMin, askMax, bidMin, bidMax);

        HttpEntity<StockDto> requestEntity = new HttpEntity<>(stockModel, header);

        RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        template.patchForObject(uri, requestEntity, StockDto.class);
    }

    // retorno 0 = fechamento de order de compra
    // retorno 1 = fechamento de order de venda
    // retorno 2 = fechamento de ambas
    private Integer transferencias(UserOrder orderCompra, UserOrder orderVenda) {

        User userComprador = userService.buscarOuFalhar(orderCompra.getIdUser());
        User userVendedor = userService.buscarOuFalhar(orderVenda.getIdUser());

        UserStockBalance userStockComprador;
        UserStockBalance userStockVendedor;

        UserStockBalancePKId compradorStockId = new UserStockBalancePKId(orderCompra.getIdUser(),
                orderCompra.getIdStock());
        UserStockBalancePKId vendedorStockId = new UserStockBalancePKId(orderVenda.getIdUser(),
                orderVenda.getIdStock());

        Long volumeCompra = orderCompra.getVolumeRemaining();
        Long volumeVenda = orderVenda.getVolumeRemaining();

        if (volumeCompra < volumeVenda) {
            // condicao que fechara a ordem de compra - retorna 0
            // transferencia entre orders
            orderCompra.subtraiVolumeRemaining(volumeCompra);
            orderVenda.subtraiVolumeRemaining(volumeCompra);

            // transferencia entre user stocks
            if (userStockService.existsById(compradorStockId)) {
                userStockComprador = userStockService.buscarOuFalhar(compradorStockId);
            } else {
                userStockComprador = new UserStockBalance();
                userStockComprador.setIdUser(orderCompra.getIdUser());
                userStockComprador.setIdStock(orderCompra.getIdStock());
                userStockComprador.setStockSymbol(orderCompra.getStockSymbol());
                userStockComprador.setStockName(orderCompra.getStockName());
                userStockComprador.setVolume(0L);
                userStockComprador = userStockService.adicionar(userStockComprador);
            }

            userStockVendedor = userStockService.buscarOuFalhar(vendedorStockId);

            userStockComprador.somaVolume(volumeCompra);
            userStockVendedor.subtraiVolume(volumeCompra);

            userStockService.adicionar(userStockComprador);
            userStockService.adicionar(userStockVendedor);

            // transferencia entre users / dollares
            BigDecimal valor = calcularPriceVolume(orderVenda.getPrice(), volumeVenda);
            userComprador.subtraiDollarBalance(valor);
            userVendedor.somaDollarBalance(valor);
            userService.adicionar(userComprador);
            userService.adicionar(userVendedor);

            // fechar userorder
            userOrderRepository.save(orderVenda);
            orderCompra.fechaOrder();
            userOrderRepository.save(orderCompra);
            // retorno 0 = fechamento da order de compra
            return 0;

        } else if (volumeCompra > volumeVenda) {
            // condicao que fechara a ordem de venda - retorna 1
            // transferencia entre orders
            orderCompra.subtraiVolumeRemaining(volumeVenda);
            orderVenda.subtraiVolumeRemaining(volumeVenda);

            // transferencia entre user stocks
            if (userStockService.existsById(compradorStockId)) {
                userStockComprador = userStockService.buscarOuFalhar(compradorStockId);
            } else {
                userStockComprador = new UserStockBalance();
                userStockComprador.setIdUser(orderCompra.getIdUser());
                userStockComprador.setIdStock(orderCompra.getIdStock());
                userStockComprador.setStockSymbol(orderCompra.getStockSymbol());
                userStockComprador.setStockName(orderCompra.getStockName());
                userStockComprador.setVolume(0L);
                userStockComprador = userStockService.adicionar(userStockComprador);
            }

            userStockVendedor = userStockService.buscarOuFalhar(vendedorStockId);

            userStockComprador.somaVolume(volumeVenda);
            userStockVendedor.subtraiVolume(volumeVenda);

            userStockService.adicionar(userStockComprador);
            userStockService.adicionar(userStockVendedor);

            // transferencia entre users / dollares
            BigDecimal valor = calcularPriceVolume(orderVenda.getPrice(), volumeVenda);
            userComprador.subtraiDollarBalance(valor);
            userVendedor.somaDollarBalance(valor);
            userService.adicionar(userComprador);
            userService.adicionar(userVendedor);

            // fechar userorder
            userOrderRepository.save(orderCompra);
            orderVenda.fechaOrder();
            userOrderRepository.save(orderVenda);
            // retorno 1 = fechamento da order de venda
            return 1;

        } else {
            // condicao que fechara ambas as orders (volumeCompra == volumeVenda) - retorna
            // 2
            // transferencia entre orders
            orderCompra.subtraiVolumeRemaining(volumeVenda);
            orderVenda.subtraiVolumeRemaining(volumeCompra);

            // transferencia entre user stocks
            if (userStockService.existsById(compradorStockId)) {
                userStockComprador = userStockService.buscarOuFalhar(compradorStockId);
            } else {
                userStockComprador = new UserStockBalance();
                userStockComprador.setIdUser(orderCompra.getIdUser());
                userStockComprador.setIdStock(orderCompra.getIdStock());
                userStockComprador.setStockSymbol(orderCompra.getStockSymbol());
                userStockComprador.setStockName(orderCompra.getStockName());
                userStockComprador.setVolume(0L);
                userStockComprador = userStockService.adicionar(userStockComprador);
            }

            userStockVendedor = userStockService.buscarOuFalhar(vendedorStockId);

            userStockComprador.somaVolume(volumeVenda);
            userStockVendedor.subtraiVolume(volumeCompra);

            userStockService.adicionar(userStockComprador);
            userStockService.adicionar(userStockVendedor);

            // transferencia entre users / dollares
            BigDecimal valor = calcularPriceVolume(orderVenda.getPrice(), volumeVenda);
            userComprador.subtraiDollarBalance(valor);
            userVendedor.somaDollarBalance(valor);
            userService.adicionar(userComprador);
            userService.adicionar(userVendedor);

            // fechar userorder
            orderCompra.fechaOrder();
            orderVenda.fechaOrder();
            userOrderRepository.save(orderCompra);
            userOrderRepository.save(orderVenda);
            // retorno 2 = fechamento de ambas as orders
            return 2;
        }
    }
}
