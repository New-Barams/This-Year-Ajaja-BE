package com.newbarams.ajaja.global.config;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	private static final String JWT = "JWT";

	@Bean
	OpenAPI ajajaAPI() {
		return new OpenAPI()
			.info(ajajaInfo())
			.components(securityComponent())
			.addSecurityItem(securityItem());
	}

	private Info ajajaInfo() {
		return new Info().title("올해도 아좌좌🔥")
			.description("뉴바람스의 [올해도 아좌좌🔥] API 문서입니다.")
			.contact(contact())
			.version("v1");
	}

	private Contact contact() {
		return new Contact()
			.name("Hejow")
			.url("https://github.com/New-Barams/This-Year-Ajaja-BE")
			.email("gmlwh124@naver.com");
	}

	private Components securityComponent() {
		return new Components().addSecuritySchemes(JWT, bearerScheme());
	}

	private SecurityScheme bearerScheme() {
		return new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat(JWT)
			.in(SecurityScheme.In.HEADER)
			.name(AUTHORIZATION);
	}

	private SecurityRequirement securityItem() {
		return new SecurityRequirement().addList(JWT);
	}
}
