package com.monitoring.data_manipulation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableEurekaClient
@SpringBootApplication
public class DataManipulationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataManipulationApplication.class, args);
    }

}
