package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration .class})

public class GatewayApplicaion {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplicaion.class, args);


    }
}
