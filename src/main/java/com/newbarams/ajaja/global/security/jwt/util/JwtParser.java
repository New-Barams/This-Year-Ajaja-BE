package com.newbarams.ajaja.global.security.jwt.util;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.global.security.common.CustomUserDetailService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtParser {
	private final JwtSecretProvider jwtSecretProvider;
	private final CustomUserDetailService userDetailService;
	private final io.jsonwebtoken.JwtParser parser;

	public JwtParser(JwtSecretProvider jwtSecretProvider, CustomUserDetailService userDetailService) {
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
		Claims claims = parseClaim(jwt);
		return claims.get(jwtSecretProvider.getSignature(), Long.class);
	}

	private Claims parseClaim(String jwt) {
		try {
			return parser.parseSignedClaims(jwt).getPayload();
		} catch (SecurityException | MalformedJwtException e) {
			throw new AjajaException(INVALID_JWT_SIGNATURE);
		} catch (ExpiredJwtException e) {
			throw new AjajaException(EXPIRED_JWT);
		} catch (UnsupportedJwtException e) {
			throw new AjajaException(UNSUPPORTED_JWT);
		} catch (IllegalArgumentException e) {
			throw new AjajaException(INVALID_JWT);
		}
	}
}
