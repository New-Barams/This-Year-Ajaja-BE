package com.newbarams.ajaja.global.security.jwt.util;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.global.security.common.CustomUserDetailService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;

@Component
public class JwtParser {
	private final JwtSecretProvider jwtSecretProvider;
	private final CustomUserDetailService userDetailService;
	private final io.jsonwebtoken.JwtParser parser;

	JwtParser(JwtSecretProvider jwtSecretProvider, CustomUserDetailService userDetailService) {
		this.jwtSecretProvider = jwtSecretProvider;
		this.userDetailService = userDetailService;
		this.parser = Jwts.parser().verifyWith(jwtSecretProvider.getSecretKey()).build();
	}

	public Authentication parseAuthentication(String jwt) {
		Long userId = parseId(jwt);
		UserDetails user = userDetailService.loadUserByUsername(String.valueOf(userId)); // todo: ridiculous code
		return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}

	public Long parseId(String jwt) {
		Claims claims = parseClaim(jwt).orElseThrow(() -> new AjajaException(EMPTY_JWT));
		return claims.get(jwtSecretProvider.getSignature(), Long.class);
	}

	private Optional<Claims> parseClaim(String jwt) {
		try {
			return Optional.ofNullable(parser.parseSignedClaims(jwt).getPayload());
		} catch (JwtException | IllegalArgumentException e) {
			handleParseException(e);
		}

		return Optional.empty();
	}

	private void handleParseException(RuntimeException exception) {
		handleBadSignature(exception);
		handleExpiredToken(exception);
		handleUnsupportedToken(exception);
		handleEmptyClaim(exception);
	}

	private void handleBadSignature(RuntimeException exception) {
		if (exception instanceof SecurityException || exception instanceof MalformedJwtException) {
			throw new AjajaException(INVALID_JWT_SIGNATURE);
		}
	}

	private void handleExpiredToken(RuntimeException exception) {
		if (exception instanceof ExpiredJwtException) {
			throw new AjajaException(EXPIRED_JWT);
		}
	}

	private void handleUnsupportedToken(RuntimeException exception) {
		if (exception instanceof UnsupportedJwtException) {
			throw new AjajaException(UNSUPPORTED_JWT);
		}
	}

	private void handleEmptyClaim(RuntimeException exception) {
		if (exception instanceof IllegalArgumentException) {
			throw new AjajaException(EMPTY_JWT);
		}
	}
}
