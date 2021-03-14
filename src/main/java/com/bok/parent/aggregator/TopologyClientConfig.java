package com.bok.parent.aggregator;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class TopologyClientConfig {

    //@Value("${topology.krypto.hostname}")
    //private String kryptoHostname;

    //@Value("${topology.krypto.port}")
    //private Integer kryptoPort;

}
