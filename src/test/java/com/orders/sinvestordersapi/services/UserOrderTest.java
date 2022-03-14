package com.orders.sinvestordersapi.services;

import com.orders.sinvestordersapi.domain.model.UserOrder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserOrderTest {

    private UserOrder userOrder = new UserOrder();

    @Test
    void somarVolume() {
        userOrder.setVolumeRemaining(0L);
        userOrder.somaVolumeRemaining(10L);
        Long volume = userOrder.getVolumeRemaining();
        Assertions.assertEquals(volume, 10, "O resultado da soma é diferente do esperado");
    }

    @Test
    void subtrairVolume() {
        userOrder.setVolumeRemaining(10L);
        userOrder.subtraiVolumeRemaining(10L);
        Long volume = userOrder.getVolumeRemaining();
        Assertions.assertEquals(volume, 0, "O resultado da subtração é diferente do esperado");
    }

    @Test
    void failsubtrairVolume() {
        userOrder.setVolumeRemaining(10L);
        userOrder.subtraiVolumeRemaining(10L);
        Long volume = userOrder.getVolumeRemaining();
        Assertions.assertNotEquals(volume, 5, "O resultado da subtração é igual ao esperado");
    }

}
