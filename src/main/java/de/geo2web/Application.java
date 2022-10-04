package de.geo2web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static final String CrossOriginUrl = "http://localhost:4200";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
