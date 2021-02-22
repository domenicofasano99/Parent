package com.bok.parent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.bok.parent.repository")
public class ParentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParentApplication.class, args);
    }

}
