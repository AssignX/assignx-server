package com.assignx.AssignxServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AssignxServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignxServerApplication.class, args);
    }

}
