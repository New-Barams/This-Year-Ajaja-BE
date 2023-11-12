package com.newbarams.ajaja.global.security.jwt.filter;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;
import static org.springframework.http.HttpHeaders.*;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.global.security.jwt.util.JwtParser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String BEARER_PREFIX = "Bearer ";

	private final List<String> allowList;
	private final JwtParser jwtParser;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		authenticateRequest(request);
		filterChain.doFilter(request, response);
	}

	private void authenticateRequest(HttpServletRequest request) {
		if (isSecureUri(request.getRequestURI())) {
			String jwt = resolveJwtFromRequest(request);
			authenticate(jwt);
		}
	}

	private boolean isSecureUri(String requestUri) {
		return allowList.stream().noneMatch(requestUri::contains);
	}

	private String resolveJwtFromRequest(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION);
		validateBearerToken(token);
		return resolve(token);
	}

	private void validateBearerToken(String bearerToken) {
		if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
			throw new AjajaException(INVALID_BEARER_FORMAT);
		}
	}

	private String resolve(String bearerToken) {
		return bearerToken.substring(BEARER_PREFIX.length());
	}

	private void authenticate(String jwt) {
		Authentication authentication = jwtParser.parseAuthentication(jwt);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
