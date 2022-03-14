package com.orders.sinvestordersapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class SinvestOrdersApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinvestOrdersApiApplication.class, args);

    }

    @Configuration
    @EnableWebSecurity
    class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and()
                    .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                    .oauth2ResourceServer().jwt();
        }
    }

}
