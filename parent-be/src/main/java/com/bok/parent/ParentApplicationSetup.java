package com.bok.parent;


import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableJpaRepositories({"com.bok.parent.repository", "com.bok.parent.geolocalization.repository"})
@EnableSwagger2
@EnableEurekaClient
@EnableZuulProxy
@EnableAsync
@EnableScheduling
@Configuration
public class ParentApplicationSetup {
}
