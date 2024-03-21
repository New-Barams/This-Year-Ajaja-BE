package me.ajaja.global.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.security.interceptor.AuthorizationInterceptor;

@Configuration
@RequiredArgsConstructor
public class SecurityWebConfig implements WebMvcConfigurer {
	private final AuthorizationInterceptor authorizationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationInterceptor);
	}
}
