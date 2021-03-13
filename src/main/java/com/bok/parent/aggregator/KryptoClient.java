package com.bok.parent.aggregator;

import com.bok.integration.krypto.dto.PriceResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "KryptoClient", url = "http://krypto:8080/")
public interface KryptoClient {
    @GetMapping(value = "/price/{symbol}", consumes = MediaType.APPLICATION_JSON_VALUE)
    PriceResponseDTO getPriceBySymbol(@PathVariable("symbol") String symbol);

}
