package com.folaolaitan.bondcatalog.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.folaolaitan.bondcatalog.entity.Bond;
import com.folaolaitan.bondcatalog.repository.BondRepository;

// Data initialization configuration to preload bond data into the database
// It pre-populates the database with 30 sample bonds, ensuring a variety of scenarios for testing.
@Configuration
public class DataInitializer {
    // Helper: insert only if unique by (name, issuer, maturityDate)
    private void insertIfMissing(BondRepository repo, Bond b) {
        if (!repo.existsByNameAndIssuerAndMaturityDate(b.getName(), b.getIssuer(), b.getMaturityDate())) {
            repo.save(b);
        }
    }

    // CommandLineRunner to preload data.
    // CommandLineRunner is a functional interface that can be used to execute code at startup.
    @Bean
    CommandLineRunner commandLineRunner(BondRepository bondRepository) {
        return args -> {
            // ----- Base 5 (kept) -----
            Bond b1 = new Bond(null, "US Treasury Bond 10Y", "US Government",
                    new BigDecimal("1000.00"), new BigDecimal("3.25"), "AAA",
                    LocalDate.of(2023, 1, 1), LocalDate.of(2033, 12, 31), "USD");           // Active
            insertIfMissing(bondRepository, b1);

            Bond b2 = new Bond(null, "Corporate Bond - Apple Inc", "Apple Inc",
                    new BigDecimal("5000.00"), new BigDecimal("4.50"), "AA+",
                    LocalDate.of(2022, 4, 15), LocalDate.of(2030, 6, 30), "USD");           // Active
            insertIfMissing(bondRepository, b2);

            Bond b3 = new Bond(null, "Green Energy Bond", "GreenFuture Ltd",
                    new BigDecimal("2000.00"), new BigDecimal("5.00"), "A",
                    LocalDate.of(2021, 8, 10), LocalDate.of(2029, 3, 15), "USD");           // Active
            insertIfMissing(bondRepository, b3);

            Bond b4 = new Bond(null, "Emerging Market Bond", "Govt of Nigeria",
                    new BigDecimal("1000.00"), new BigDecimal("7.00"), "BB",
                    LocalDate.of(2020, 11, 1), LocalDate.of(2031, 10, 20), "NGN");          // Active (NG)
            insertIfMissing(bondRepository, b4);

            Bond b5 = new Bond(null, "Eurobond - Mercedes Benz", "Mercedes Benz AG",
                    new BigDecimal("3000.00"), new BigDecimal("2.75"), "AA",
                    LocalDate.of(2019, 3, 25), LocalDate.of(2028, 9, 1), "EUR");            // Active (near)
            insertIfMissing(bondRepository, b5);

            // ----- 25 more (with controlled statuses) -----

            Bond b6 = new Bond(null, "UK Gilt 5Y", "UK Government",
                    new BigDecimal("1000.00"), new BigDecimal("2.50"), "AAA",
                    LocalDate.of(2016, 6, 1), LocalDate.of(2021, 6, 1), "GBP");             // Matured
            insertIfMissing(bondRepository, b6);

            Bond b7 = new Bond(null, "Corporate Bond - Amazon", "Amazon Inc",
                    new BigDecimal("4000.00"), new BigDecimal("3.80"), "AA",
                    LocalDate.of(2022, 9, 15), LocalDate.of(2032, 9, 15), "USD");           // Active
            insertIfMissing(bondRepository, b7);

            Bond b8 = new Bond(null, "Municipal Bond - California", "State of California",
                    new BigDecimal("1500.00"), new BigDecimal("2.20"), "A+",
                    LocalDate.of(2010, 1, 1), LocalDate.of(2020, 1, 1), "USD");             // Matured
            insertIfMissing(bondRepository, b8);

            Bond b9 = new Bond(null, "Japanese Government Bond 15Y", "Govt of Japan",
                    new BigDecimal("2000.00"), new BigDecimal("0.80"), "AAA",
                    LocalDate.of(2020, 2, 1), LocalDate.of(2035, 2, 1), "JPY");             // Active
            insertIfMissing(bondRepository, b9);

            Bond b10 = new Bond(null, "Corporate Bond - Tesla", "Tesla Inc",
                    new BigDecimal("3500.00"), new BigDecimal("6.00"), "BBB",
                    LocalDate.of(2021, 5, 10), LocalDate.of(2031, 5, 10), "USD");           // Active
            insertIfMissing(bondRepository, b10);

            Bond b11 = new Bond(null, "Green Bond - Siemens", "Siemens AG",
                    new BigDecimal("2500.00"), new BigDecimal("2.90"), "AA",
                    LocalDate.of(2014, 3, 20), LocalDate.of(2021, 3, 20), "EUR");           // Matured
            insertIfMissing(bondRepository, b11);

            Bond b12 = new Bond(null, "Sovereign Bond - Ghana", "Govt of Ghana",
                    new BigDecimal("1000.00"), new BigDecimal("8.25"), "B",
                    LocalDate.of(2021, 7, 1), LocalDate.of(2031, 7, 1), "GHS");             // Active
            insertIfMissing(bondRepository, b12);

            Bond b13 = new Bond(null, "Corporate Bond - Google", "Alphabet Inc",
                    new BigDecimal("6000.00"), new BigDecimal("3.40"), "AA+",
                    LocalDate.of(2023, 5, 15), LocalDate.of(2033, 5, 15), "USD");           // Active
            insertIfMissing(bondRepository, b13);

            Bond b14 = new Bond(null, "Corporate Bond - Microsoft", "Microsoft Corp",
                    new BigDecimal("5500.00"), new BigDecimal("3.75"), "AAA",
                    LocalDate.of(2011, 11, 1), LocalDate.of(2021, 11, 1), "USD");           // Matured
            insertIfMissing(bondRepository, b14);

            Bond b15 = new Bond(null, "Municipal Bond - New York City", "NYC Government",
                    new BigDecimal("1200.00"), new BigDecimal("2.60"), "A",
                    LocalDate.of(2020, 8, 1), LocalDate.of(2030, 8, 1), "USD");             // Active
            insertIfMissing(bondRepository, b15);

            Bond b16 = new Bond(null, "Eurobond - BMW", "BMW AG",
                    new BigDecimal("2800.00"), new BigDecimal("2.50"), "A+",
                    LocalDate.of(2021, 4, 15), LocalDate.of(2028, 4, 15), "EUR");           // Active (near)
            insertIfMissing(bondRepository, b16);

            Bond b17 = new Bond(null, "Corporate Bond - Netflix", "Netflix Inc",
                    new BigDecimal("2200.00"), new BigDecimal("5.75"), "BB+",
                    LocalDate.of(2013, 6, 10), LocalDate.of(2020, 6, 10), "USD");           // Matured
            insertIfMissing(bondRepository, b17);

            Bond b18 = new Bond(null, "Sovereign Bond - Kenya", "Govt of Kenya",
                    new BigDecimal("1000.00"), new BigDecimal("9.00"), "B",
                    LocalDate.of(2020, 1, 1), LocalDate.of(2030, 1, 1), "KES");             // Active
            insertIfMissing(bondRepository, b18);

            Bond b19 = new Bond(null, "Eurobond - Nestle", "Nestle SA",
                    new BigDecimal("2700.00"), new BigDecimal("2.40"), "AA",
                    LocalDate.of(2012, 9, 1), LocalDate.of(2019, 9, 1), "EUR");             // Matured
            insertIfMissing(bondRepository, b19);

            Bond b20 = new Bond(null, "Corporate Bond - Meta", "Meta Platforms Inc",
                    new BigDecimal("3300.00"), new BigDecimal("4.10"), "A",
                    LocalDate.of(2022, 2, 15), LocalDate.of(2032, 2, 15), "USD");           // Active
            insertIfMissing(bondRepository, b20);

            Bond b21 = new Bond(null, "Sovereign Bond - South Africa", "Govt of South Africa",
                    new BigDecimal("1000.00"), new BigDecimal("10.50"), "BB",
                    LocalDate.of(2020, 5, 1), LocalDate.of(2030, 5, 1), "ZAR");             // Active
            insertIfMissing(bondRepository, b21);

            Bond b22 = new Bond(null, "Corporate Bond - Samsung", "Samsung Electronics",
                    new BigDecimal("3100.00"), new BigDecimal("3.20"), "A+",
                    LocalDate.of(2021, 10, 1), LocalDate.of(2031, 10, 1), "KRW");           // Active
            insertIfMissing(bondRepository, b22);

            Bond b23 = new Bond(null, "Infrastructure Bond", "World Bank",
                    new BigDecimal("4000.00"), new BigDecimal("2.10"), "AAA",
                    LocalDate.of(2023, 1, 1), LocalDate.of(2038, 1, 1), "USD");             // Active
            insertIfMissing(bondRepository, b23);

            Bond b24 = new Bond(null, "Corporate Bond - CocaCola", "CocaCola Inc",
                    new BigDecimal("2600.00"), new BigDecimal("3.60"), "A+",
                    LocalDate.of(2014, 12, 15), LocalDate.of(2022, 12, 15), "USD");         // Matured
            insertIfMissing(bondRepository, b24);

            Bond b25 = new Bond(null, "Sustainability-Linked Bond - Unilever", "Unilever PLC",
                    new BigDecimal("2400.00"), new BigDecimal("2.95"), "A",
                    LocalDate.of(2021, 3, 1), LocalDate.of(2029, 3, 1), "GBP");             // Active
            insertIfMissing(bondRepository, b25);

            // ---- Add 5 more to reach 30, with explicit Defaulted & more NG issuers ----

            // Nigeria (Eurobond USD) - Defaulted
            Bond b26 = new Bond(null, "Nigeria Eurobond 2027", "Govt of Nigeria",
                    new BigDecimal("1000.00"), new BigDecimal("8.75"), "CCC",
                    LocalDate.of(2019, 6, 1), LocalDate.of(2027, 6, 1), "USD");             // Active by date, force Defaulted
            b26.setStatus("Defaulted");
            insertIfMissing(bondRepository, b26);

            // Nigeria (Corporate NGN) - Active
            Bond b27 = new Bond(null, "Dangote Cement 2029", "Dangote Cement Plc",
                    new BigDecimal("5000.00"), new BigDecimal("12.50"), "BBB",
                    LocalDate.of(2022, 1, 1), LocalDate.of(2029, 12, 31), "NGN");           // Active
            insertIfMissing(bondRepository, b27);

            // Nigeria (State Govt NGN) - Matured
            Bond b28 = new Bond(null, "Lagos State 2020 Series", "Lagos State Government",
                    new BigDecimal("2000.00"), new BigDecimal("14.00"), "BB",
                    LocalDate.of(2015, 1, 1), LocalDate.of(2020, 12, 31), "NGN");           // Matured
            insertIfMissing(bondRepository, b28);

            // Corporate - High yield, Defaulted
            Bond b29 = new Bond(null, "High Yield Corp 2028", "Acme High Yield Ltd",
                    new BigDecimal("1500.00"), new BigDecimal("11.00"), "CCC",
                    LocalDate.of(2021, 9, 1), LocalDate.of(2028, 9, 1), "USD");             // Active by date, force Defaulted
            b29.setStatus("Defaulted");
            insertIfMissing(bondRepository, b29);

            // EU Corporate - Matured
            Bond b30 = new Bond(null, "Eurobond - Airbus 2019/2024", "Airbus SE",
                    new BigDecimal("3000.00"), new BigDecimal("1.80"), "A",
                    LocalDate.of(2019, 5, 1), LocalDate.of(2024, 5, 1), "EUR");             // Matured
            insertIfMissing(bondRepository, b30);

            System.out.println("✅ 30 sample bonds inserted successfully.");
        };
    }
}
