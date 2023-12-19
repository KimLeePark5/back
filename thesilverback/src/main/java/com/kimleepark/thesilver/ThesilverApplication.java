package com.kimleepark.thesilver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ThesilverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThesilverApplication.class, args);
    }

}
