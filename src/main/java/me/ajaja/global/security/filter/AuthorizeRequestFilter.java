package me.ajaja.global.security.filter;

import static me.ajaja.global.exception.ErrorCode.*;

import java.io.IOException;
import java.util.List;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.ajaja.global.exception.AjajaException;

public class AuthorizeRequestFilter extends OncePerRequestFilter {
	private static final List<String> ALLOW_LIST = List.of(
		"/index.html", "/swagger-spec", "/swagger-ui", "/api-docs", "favicon", // swagger
		"/login", "/reissue", // auth api
		"/users", "/plans", "/reminds", "/feedbacks" // domain apis
	);

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String requestUri = request.getRequestURI();
		validateUri(requestUri);
		filterChain.doFilter(request, response);
	}

	private void validateUri(String requestUri) {
		if (isNotSupport(requestUri)) {
			throw new AjajaException(NOT_SUPPORT_END_POINT);
		}
	}

	private static boolean isNotSupport(String requestUri) {
		return ALLOW_LIST.stream().noneMatch(requestUri::contains);
	}
}
