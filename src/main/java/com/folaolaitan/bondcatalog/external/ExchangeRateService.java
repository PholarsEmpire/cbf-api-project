
package com.folaolaitan.bondcatalog.external;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.folaolaitan.bondcatalog.config.ExchangeAPIProperties;
import com.folaolaitan.bondcatalog.customexceptions.BadRequestException;


// Service class for fetching foreign exchange rates from an external API
@Service
public class ExchangeRateService {

    private final WebClient fxWebClient;
    private final ExchangeAPIProperties exchangeAPIProperties;

    @Value("${exchange.api.key:}")
    private String apiKey; // may be empty

    public ExchangeRateService(WebClient.Builder builder, ExchangeAPIProperties exchangeAPIProperties) {
        this.fxWebClient = builder.baseUrl(exchangeAPIProperties.getUrl()).build();
        this.exchangeAPIProperties = exchangeAPIProperties;
    }

    /**
     * Returns FX rate FROM base TO target using /convert.
     * Example external call: /convert?from=USD&to=NGN&amount=1&access_key=XXXX
     */
    @SuppressWarnings("unchecked")
    @Cacheable(cacheNames = "fxRates", key = "#base.toUpperCase() + ':' + #target.toUpperCase()")
    public BigDecimal getRate(String base, String target) {
        if (base == null || target == null || base.isBlank() || target.isBlank()) {
            throw new BadRequestException("Base and target currencies are required.");
        }

        String from = base.toUpperCase();
        String to   = target.toUpperCase();

        Map<String, Object> resp = fxWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/convert")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .queryParam("amount", 1)
                        .queryParam("access_key", exchangeAPIProperties.getKey())
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // Expected shape:
        // { "success": true, "query": {...}, "info": {"rate": 1530.25}, "result": 1530.25 }
        if (resp == null || Boolean.FALSE.equals(resp.get("success"))) {
            throw new BadRequestException("Failed to retrieve FX rate" +resp);
        }

        Object infoObj = resp.get("info");
        if (infoObj instanceof Map<?, ?> info && info.get("rate") != null) {
            return new BigDecimal(info.get("rate").toString());
        }

        Object result = resp.get("result");
        if (result == null) {
            throw new BadRequestException("FX response missing rate for " + from + " -> " + to);
        }
        return new BigDecimal(result.toString());
    }
}
