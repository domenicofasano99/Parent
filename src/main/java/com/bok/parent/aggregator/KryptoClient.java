package com.bok.parent.aggregator;

import com.bok.parent.aggregator.dto.PriceResponseDTO;
import feign.Param;
import feign.RequestLine;

public interface KryptoClient {
    @RequestLine("GET /price/{symbol}")
    PriceResponseDTO getPriceBySymbol(@Param("symbol") String symbol);

}
