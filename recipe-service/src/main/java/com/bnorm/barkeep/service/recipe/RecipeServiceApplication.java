package com.bnorm.barkeep.service.recipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RecipeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeServiceApplication.class, args);
    }
}

