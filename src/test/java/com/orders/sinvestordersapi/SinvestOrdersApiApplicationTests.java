package com.orders.sinvestordersapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SinvestOrdersApiApplicationTests {

    @Test
    void contextLoads() {
        Long num = 1L;
        Assertions.assertEquals(1, num, "Ok");
    }

}
