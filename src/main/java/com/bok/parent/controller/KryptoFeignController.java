package com.bok.parent.controller;

import com.bok.integration.krypto.dto.PriceResponseDTO;
import com.bok.parent.aggregator.KryptoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class KryptoFeignController {

    @Autowired
    KryptoClient kryptoClient;

    @GetMapping("/price/{symbol}")
    public PriceResponseDTO getPrice(@PathVariable("symbol") String symbol) {
        log.info("Received request for price of:{}", symbol);
        return kryptoClient.getPriceBySymbol(symbol);
    }
}
