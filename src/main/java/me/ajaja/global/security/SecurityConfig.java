package me.ajaja.global.security;

import static org.springframework.security.config.Customizer.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.security.filter.AuthorizationExceptionFilter;
import me.ajaja.global.security.filter.AuthorizeRequestFilter;
import me.ajaja.global.security.jwt.JwtParser;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final ObjectMapper objectMapper;
	private final JwtParser jwtParser; // todo: tmp

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.cors(withDefaults())
			.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http
			.addFilterBefore(new AuthorizeRequestFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new AuthorizationExceptionFilter(objectMapper), AuthorizeRequestFilter.class);

		http.authorizeHttpRequests(request -> request.anyRequest().permitAll());

		return http.build();
	}
}
