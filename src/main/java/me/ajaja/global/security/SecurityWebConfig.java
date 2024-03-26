package me.ajaja.global.security;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.security.interceptor.AuthorizationInterceptor;
import me.ajaja.global.security.resolver.AuthenticatedUserArgumentResolver;

@Configuration
@RequiredArgsConstructor
public class SecurityWebConfig implements WebMvcConfigurer {
	private final AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver;
	private final AuthorizationInterceptor authorizationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationInterceptor);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authenticatedUserArgumentResolver);
	}
}
