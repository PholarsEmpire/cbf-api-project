package com.folaolaitan.bondcatalog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// Configuration properties for the external exchange API
@Configuration
@ConfigurationProperties(prefix = "exchange.api")
public class ExchangeAPIProperties {

    private String url;
    private String key;
    private int timeout;

    // Getters and Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
