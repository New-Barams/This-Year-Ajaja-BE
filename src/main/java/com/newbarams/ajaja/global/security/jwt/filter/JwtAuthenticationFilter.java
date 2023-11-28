package com.newbarams.ajaja.global.security.jwt.filter;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static com.newbarams.ajaja.global.util.BearerTokenUtil.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.security.jwt.util.JwtParser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String PLAN_URI = "/plans";

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
		if (isSecureUri(request)) {
			String jwt = resolveJwtFromRequest(request);
			authenticate(jwt);
		}
	}

	private boolean isSecureUri(HttpServletRequest request) {
		return allowList.stream().noneMatch(request.getRequestURI()::contains) && isNotGetPlans(request);
	}

	private boolean isNotGetPlans(HttpServletRequest request) { // todo: separate filtering this request
		return !(PLAN_URI.equals(request.getRequestURI()) && GET.matches(request.getMethod()));
	}

	private String resolveJwtFromRequest(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION);
		validateBearerToken(token);
		return resolveJwt(token);
	}

	private void validateBearerToken(String bearerToken) {
		if (isNotBearerToken(bearerToken)) {
			throw new AjajaException(INVALID_BEARER_TOKEN);
		}
	}

	private boolean isNotBearerToken(String token) {
		return !(StringUtils.hasText(token) && isBearer(token));
	}

	private void authenticate(String jwt) {
		Authentication authentication = jwtParser.parseAuthentication(jwt);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
