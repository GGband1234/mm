package org.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.client")
public class PurchasesApplication {
    public static void main(String[] args) {
         SpringApplication.run(PurchasesApplication.class, args);


    }
}