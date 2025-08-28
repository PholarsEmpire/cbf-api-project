package com.folaolaitan.bondcatalog.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.folaolaitan.bondcatalog.entity.Bond;
import com.folaolaitan.bondcatalog.repository.BondRepository;

@Configuration
public class DataInitializer {
        // Helper method to insert bond only if it doesn't already exist
        private void insertIfMissing(BondRepository repo, Bond b) {
                if (!repo.existsByNameAndIssuerAndMaturityDate(b.getName(), b.getIssuer(), b.getMaturityDate())) {
                repo.save(b);
                }
         }

    @Bean
    CommandLineRunner commandLineRunner(BondRepository bondRepository) {
        return args -> {
            // Initial data to test mys API

            insertIfMissing(bondRepository, new Bond(
                    null,
                    "US Treasury Bond 10Y",
                    "US Government",
                    new BigDecimal("1000.00"),
                    new BigDecimal("3.25"),
                    "AAA",
                    LocalDate.of(2023, 1, 1),
                    LocalDate.of(2033, 12, 31), "USD"
            ));

            insertIfMissing(bondRepository, new Bond(
                    null,
                    "Corporate Bond - Apple Inc",
                    "Apple Inc",
                    new BigDecimal("5000.00"),
                    new BigDecimal("4.50"),
                    "AA+",
                    LocalDate.of(2022, 4, 15),
                    LocalDate.of(2030, 6, 30), "USD"
            ));

            insertIfMissing(bondRepository, new Bond(
                    null,
                    "Green Energy Bond",
                    "GreenFuture Ltd",
                    new BigDecimal("2000.00"),
                    new BigDecimal("5.00"),
                    "A",
                    LocalDate.of(2021, 8, 10),
                    LocalDate.of(2029, 3, 15), "USD"
            ));

            insertIfMissing(bondRepository, new Bond(
                    null,
                    "Emerging Market Bond",
                    "Govt of Nigeria",
                    new BigDecimal("1000.00"),
                    new BigDecimal("7.00"),
                    "BB",
                    LocalDate.of(2020, 11, 1),
                    LocalDate.of(2031, 10, 20), "NGN"
            ));

            insertIfMissing(bondRepository, new Bond(
                    null,
                    "Eurobond - Mercedes Benz",
                    "Mercedes Benz AG",
                    new BigDecimal("3000.00"),
                    new BigDecimal("2.75"),
                    "AA",
                    LocalDate.of(2019, 3, 25),
                    LocalDate.of(2028, 9, 1), "EUR"
            ));

            System.out.println("✅ Sample bonds with issue/maturity dates inserted.");
        };
    }
    

}
