package com.example.mlservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MlServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MlServiceApplication.class, args);
    }

}
