package com.bok.parent.aggregator;

import feign.Feign;
import feign.Logger;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class TopologyClientConfig {

    @Value("${topology.krypto.hostname}")
    private String kryptoHostname;

    @Value("${topology.krypto.port}")
    private Integer kryptoPort;

}
