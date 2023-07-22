package com.prgrms.gccoffee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GcCoffeeBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(GcCoffeeBatchApplication.class, args);
    }
}