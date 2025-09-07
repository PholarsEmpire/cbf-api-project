package com.folaolaitan.bondcatalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// WebClient configuration for external API calls
@Configuration
public class WebClientConfig {

    @Bean(name = "fxWebClient")
    public WebClient fxWebClient(
            WebClient.Builder builder,
            @Value("${exchange.api.url}") String fxBaseUrl
    ) {
        return builder
                .baseUrl(fxBaseUrl) // e.g., https://api.exchangerate.host
                .build();
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        // general-purpose client if you ever need it
        return builder.build();
    }
}
