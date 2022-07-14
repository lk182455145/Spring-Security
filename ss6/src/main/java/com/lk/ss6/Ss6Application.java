package com.lk.ss6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Ss6Application {

    public static void main(String[] args) {
        SpringApplication.run(Ss6Application.class, args);
    }

}
