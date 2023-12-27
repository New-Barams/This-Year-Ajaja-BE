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
	private static final Pattern GET_ONE_PLAN = Pattern.compile("^/plans/\\d+$");
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
		return isNotGetPlans(request) && isNotSecuredUri(request);
	}

	private boolean isNotSecuredUri(HttpServletRequest request) {
		return allowList.stream().noneMatch(request.getRequestURI()::contains);
	}

	private boolean isNotGetPlans(HttpServletRequest request) {
		return !isGetOnePlan(request.getRequestURI()) && !isGetAllPlans(request.getRequestURI(), request.getMethod());
	}

	private boolean isGetAllPlans(String uri, String httpMethod) { // todo: separate filtering this request
		return PLAN_URI.equals(uri) && GET.matches(httpMethod);
	}

	private boolean isGetOnePlan(String uri) {
		Matcher matcher = GET_ONE_PLAN.matcher(uri);
		return matcher.matches();
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
