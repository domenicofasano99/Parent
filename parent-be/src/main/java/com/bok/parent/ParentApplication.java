package com.bok.parent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaRepositories("com.bok.parent.repository")
@EnableSwagger2
@EnableEurekaClient
@EnableZuulProxy
public class ParentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParentApplication.class, args);
    }
}
