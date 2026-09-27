package com.propertyrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PropertyRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyRentalApplication.class, args);
        System.out.println("\n=======================================================");
        System.out.println(" PROPERTY & RENTAL MANAGEMENT SYSTEM IS RUNNING!");
        System.out.println(" API Base URL   : http://localhost:8080/api");
        System.out.println(" H2 Console     : http://localhost:8080/h2-console");
        System.out.println(" Web Portal     : http://localhost:8080/index.html");
        System.out.println("=======================================================\n");
    }
}
