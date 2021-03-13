package com.bok.parent.aggregator;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopologyClientConfig {

    @Value("${topology.krypto.hostname}")
    private String kryptoHostname;

    @Value("${topology.krypto.port}")
    private Integer kryptoPort;

    @Bean(value = "kryptoClient")
    public KryptoClient setupKryptoClient() {
        KryptoClient kryptoClient = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(KryptoClient.class))
                .logLevel(Logger.Level.FULL)
                .target(KryptoClient.class, "http://" + kryptoHostname + ":" + kryptoPort + "/");

        return kryptoClient;
    }
}
