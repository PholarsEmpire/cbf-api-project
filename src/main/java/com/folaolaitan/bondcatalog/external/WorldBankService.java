// src/main/java/.../external/WorldBankService.java
package com.folaolaitan.bondcatalog.external;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

// Service class for interacting with the World Bank API to get different economic indicators for all countries
// Enrich sovereign bonds with macro data such as(GDP, inflation, debt-to-GDP).
@Service
public class WorldBankService {
  private final WebClient webClient;

  public WorldBankService(WebClient webClient) {
     this.webClient = webClient; 
  }

  // Returns the first (latest) value from the World Bank response
  @SuppressWarnings("unchecked")
  public Double indicatorValue(String countryCode, String indicator, String year) {
    String url = String.format(
      "https://api.worldbank.org/v2/country/%s/indicator/%s?format=json&per_page=1&date=%s:%s",
      countryCode, indicator, year, year);

    List<Object> resp = webClient.get()
        .uri(url)
        .retrieve()
        .bodyToMono(List.class)
        .block();

    if (resp == null || resp.size() < 2) return null;


    List<Map<String,Object>> data = (List<Map<String,Object>>) resp.get(1);
    if (data == null || data.isEmpty()) return null;

    

    //Map<String,Object> first = data.get(0);
    Object val = data.get(0).get("value");
    return (val == null) ? null : Double.valueOf(val.toString());
  }
}
