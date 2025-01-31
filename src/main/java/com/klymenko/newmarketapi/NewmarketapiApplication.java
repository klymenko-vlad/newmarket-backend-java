package com.klymenko.newmarketapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NewmarketapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewmarketapiApplication.class, args);
    }

}
