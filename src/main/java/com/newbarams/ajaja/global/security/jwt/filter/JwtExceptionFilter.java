package com.newbarams.ajaja.global.security.jwt.filter;

import static org.apache.commons.codec.CharEncoding.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbarams.ajaja.global.exception.ErrorResponse;
import com.newbarams.ajaja.global.exception.AjajaException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtExceptionFilter extends OncePerRequestFilter {
	private static final ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (AjajaException exception) {
			handleJwtException(response, exception);
		}
	}

	private void handleJwtException(HttpServletResponse response, AjajaException exception) throws IOException {
		setResponseHeader(response);
		respondJwtException(response, exception);
	}

	private void setResponseHeader(HttpServletResponse response) {
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(UTF_8);
	}

	private void respondJwtException(HttpServletResponse response, AjajaException exception) throws IOException {
		response.setStatus(exception.getHttpStatus());
		response.getWriter().write(generateResponse(exception));
	}

	private String generateResponse(AjajaException exception) throws JsonProcessingException {
		ErrorResponse response = ErrorResponse.from(exception.getErrorCode());
		return mapper.writeValueAsString(response);
	}
}
