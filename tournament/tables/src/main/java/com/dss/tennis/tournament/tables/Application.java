package com.dss.tennis.tournament.tables;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.validation.Validation;
import javax.validation.Validator;

@SpringBootApplication
public class Application {

    @Bean
    public Validator javaxValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
