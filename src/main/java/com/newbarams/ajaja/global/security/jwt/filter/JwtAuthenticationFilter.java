package com.newbarams.ajaja.global.security.jwt.filter;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.newbarams.ajaja.global.security.jwt.util.JwtParser;
import com.newbarams.ajaja.global.util.BearerUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final Pattern PLAN_END_POINT_PATTERN = Pattern.compile("^/plans/\\d+$");
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
			String token = request.getHeader(AUTHORIZATION);
			String jwt = resolveJwt(token);
			authenticate(jwt);
		}
	}

	private boolean isSecureUri(HttpServletRequest request) {
		return isNotGetPlans(request.getRequestURI(), request.getMethod()) && isNotSecuredUri(request.getRequestURI());
	}

	private boolean isNotSecuredUri(String requestUri) {
		return allowList.stream().noneMatch(requestUri::contains);
	}

	private boolean isNotGetPlans(String requestUri, String httpMethod) {
		return !isGetOnePlan(requestUri, httpMethod) && !isGetAllPlans(requestUri, httpMethod);
	}

	private boolean isGetAllPlans(String requestUri, String httpMethod) { // todo: separate filtering this request
		return PLAN_URI.equals(requestUri) && GET.matches(httpMethod);
	}

	private boolean isGetOnePlan(String requestUri, String httpMethod) {
		Matcher matcher = PLAN_END_POINT_PATTERN.matcher(requestUri);
		return matcher.matches() && GET.matches(httpMethod);
	}

	private String resolveJwt(String token) {
		BearerUtils.validate(token);
		return BearerUtils.resolve(token);
	}

	private void authenticate(String jwt) {
		Authentication authentication = jwtParser.parseAuthentication(jwt);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
