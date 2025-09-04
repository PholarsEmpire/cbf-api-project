package com.folaolaitan.bondcatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BondCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BondCatalogApplication.class, args);
	}

}
