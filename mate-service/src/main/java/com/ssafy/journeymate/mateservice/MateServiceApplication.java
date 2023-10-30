package com.ssafy.journeymate.mateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@SpringBootApplication
@EnableFeignClients
public class MateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MateServiceApplication.class, args);
    }

}
