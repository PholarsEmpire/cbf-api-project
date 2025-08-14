package com.folaolaitan.bondcatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerOpenAPIConfig {
	@Bean
	public OpenAPI customOpenAPI(){
		return new OpenAPI().info(
			new Info()
				.title("Spring Boot API Project by Fola Olaitan")
				.version("1.0.0")
				.description("A Spring Boot–powered API that enables investors and fund managers to efficiently catalog, track, and analyze fixed income assets, providing easy access to bond details, performance summaries, and issuer insights.")
			.contact(new Contact()
				.name("Fola Olaitan")
				.email("fola@folaolaitan.com")
				.url("https://www.folaolaitan.com")
			)
		)
		.externalDocs(new io.swagger.v3.oas.models.ExternalDocumentation()
			.description("For more details visit my GitHub Repository")
			.url("https://github.com/folaolaitan/bondcatalog")
		);
	}
}
