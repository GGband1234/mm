package org.example;


import org.example.client.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableFeignClients(clients = {UserClient.class})
@EnableTransactionManagement
@SpringBootApplication
public class BudgetApplication {
    public static void main(String[] args) {
         SpringApplication.run(BudgetApplication.class, args);


    }
}
