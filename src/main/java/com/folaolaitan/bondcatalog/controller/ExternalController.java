// src/main/java/com/folaolaitan/bondcatalog/controller/ExternalController.java
package com.folaolaitan.bondcatalog.controller;

import com.folaolaitan.bondcatalog.customexceptions.BadRequestException;
import com.folaolaitan.bondcatalog.entity.Bond;
import com.folaolaitan.bondcatalog.external.ExchangeRateService;
import com.folaolaitan.bondcatalog.external.WorldBankService;
import com.folaolaitan.bondcatalog.service.BondService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/external")
@CrossOrigin(origins = "http://localhost:5173")
public class ExternalController {

    private final ExchangeRateService fx;
    private final BondService bonds;
    private final WorldBankService worldBankService;

    public ExternalController(ExchangeRateService fx, BondService bonds, WorldBankService worldBankService) {
        this.fx = fx;
        this.bonds = bonds;
        this.worldBankService = worldBankService;
    }

    // --- FX simple pair endpoint ---
    @GetMapping("/fx")
    @Operation(summary = "Gets the current exchange rate between two currencies", description = "Returns the current exchange rate for a pair, e.g., from=USD&to=NGN")
    public BigDecimal getRate(@RequestParam String from, @RequestParam String to) {
        return fx.getRate(from, to);
    }

    // --- Bond face value converted to target currency ---
    @GetMapping("/bonds/{id}/value-in")
    @Operation(summary = "Convert a bond's face value to a target currency",
               description = "Converts the bond's face value from its currency to the requested currency using live FX rates.")
    public Map<String, Object> bondValueIn(@PathVariable Long id, @RequestParam String currency) {
        Bond b = bonds.getBondById(id);

        if (b.getCurrency() == null || b.getCurrency().isBlank()) {
            throw new BadRequestException("Bond " + id + " has no currency set.");
        }
        if (b.getFaceValue() == null) {
            throw new BadRequestException("Bond " + id + " has no face value set.");
        }

        BigDecimal rate = fx.getRate(b.getCurrency(), currency);
        BigDecimal converted = b.getFaceValue().multiply(rate);

        return Map.of(
                "bondId", b.getId(),
                "bondName", b.getName(),
                "fromCurrency", b.getCurrency(),
                "toCurrency", currency.toUpperCase(),
                "rate", rate,
                "originalFaceValue", b.getFaceValue(),
                "convertedFaceValue", converted
        );
    }

    // --- World Bank macro endpoints (use ISO codes like USA, NGA, GBR) ---
    @GetMapping("/macro/{country}/gdp")
    @Operation(summary = "Get GDP for a specified country", description = "World Bank GDP (current US$). Use the country's ISO code: USA, NGA, GBR, ZAR etc.")
    public Map<String, Object> gdp(@PathVariable String country,
                                   @RequestParam(defaultValue = "2022") String year) {
        Double val = worldBankService.indicatorValue(country, "NY.GDP.MKTP.CD", year);
        return Map.of(
                "country", country.toUpperCase(),
                "year", year,
                "indicator", "GDP (current US$)",
                "value", val
        );
    }

    @GetMapping("/macro/{country}/inflation")
    @Operation(summary = "Get inflation for a specified country", description = "World Bank CPI inflation (in percentage). Use the country's ISO code: USA, NGA, GBR, ZAR etc.")
    public Map<String, Object> inflation(@PathVariable String country,
                                         @RequestParam(defaultValue = "2022") String year) {
        Double val = worldBankService.indicatorValue(country, "FP.CPI.TOTL.ZG", year);
        return Map.of(
                "country", country.toUpperCase(),
                "year", year,
                "indicator", "Inflation, CPI (%)",
                "value", val
        );
    }
}
