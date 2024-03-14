package me.ajaja.global.security.filter;

import static org.eclipse.jdt.internal.compiler.util.Util.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorResponse;

@RequiredArgsConstructor
public class AuthorizationExceptionFilter extends OncePerRequestFilter {
	private final ObjectMapper mapper;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (AjajaException exception) {
			handleAuthorizationException(response, exception);
		}
	}

	private void handleAuthorizationException(
		HttpServletResponse response,
		AjajaException exception
	) throws IOException {
		setResponseHeader(response);
		respondException(response, exception);
	}

	private void setResponseHeader(HttpServletResponse response) {
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(UTF_8);
	}

	private void respondException(HttpServletResponse response, AjajaException exception) throws IOException {
		response.setStatus(exception.getHttpStatus());
		response.getWriter().write(generateBody(exception));
	}

	private String generateBody(AjajaException exception) throws JsonProcessingException {
		ErrorResponse response = ErrorResponse.from(exception.getErrorCode());
		return mapper.writeValueAsString(response);
	}
}
