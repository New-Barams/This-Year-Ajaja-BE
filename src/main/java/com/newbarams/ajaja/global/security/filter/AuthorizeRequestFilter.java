package com.newbarams.ajaja.global.security.filter;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.newbarams.ajaja.global.security.jwt.JwtParser;
import com.newbarams.ajaja.global.util.BearerUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizeRequestFilter extends OncePerRequestFilter {
	private static final Pattern PLAN_END_POINT_PATTERN = Pattern.compile("^/plans/\\d+$");
	private static final String PLAN_URI = "/plans";

	private final JwtParser jwtParser;

	private static final List<String> ALLOW_LIST = List.of(
		"/index.html", "/swagger-spec", "/swagger-ui", "/api-docs", "favicon", // swagger
		"/login", "/reissue" // auth api
		// "/users", "/plans", "/reminds", "/feedbacks" // domain apis
	);

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		// String requestUri = request.getRequestURI(); todo: tmp
		// validateUri(requestUri);
		authenticateRequest(request);
		filterChain.doFilter(request, response);
	}

	private void authenticateRequest(HttpServletRequest request) {
		if (isNotAllowList(request)) {
			String token = request.getHeader(AUTHORIZATION);
			String jwt = resolveJwt(token);
			authenticate(jwt);
		}
	}

	private boolean isNotAllowList(HttpServletRequest request) {
		return isNotGetPlans(request.getRequestURI(), request.getMethod()) && isNotSecuredUri(request.getRequestURI());
	}

	private boolean isNotSecuredUri(String requestUri) {
		return ALLOW_LIST.stream().noneMatch(requestUri::contains);
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
		BearerUtil.validate(token);
		return BearerUtil.resolve(token);
	}

	private void authenticate(String jwt) {
		Authentication authentication = jwtParser.parseAuthentication(jwt);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	// 	filterChain.doFilter(request, response);
	// }
	//
	// private void validateUri(String requestUri) {
	// 	if (isNotSupport(requestUri)) {
	// 		throw new AjajaException(NOT_SUPPORT_END_POINT);
	// 	}
	// }
	//
	// private static boolean isNotSupport(String requestUri) {
	// 	return ALLOW_LIST.stream().noneMatch(requestUri::contains);
	// }
}
