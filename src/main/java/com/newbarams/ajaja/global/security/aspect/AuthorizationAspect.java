package com.newbarams.ajaja.global.security.aspect;

import static org.springframework.http.HttpHeaders.*;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.security.annotation.Authorization;
import com.newbarams.ajaja.global.security.jwt.JwtParser;
import com.newbarams.ajaja.global.util.BearerUtil;
import com.newbarams.ajaja.global.util.RequestUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Aspect of authorization. Resolve jwt from request header then validate.
 * If validation success set authentication on context.
 * @see Authorization
 * @author hejow
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {
	private final JwtParser jwtParser;

	@SuppressWarnings("unused")
	@Before("@annotation(com.newbarams.ajaja.global.security.annotation.Authorization)")
	public void authorization(JoinPoint joinPoint) {
		HttpServletRequest request = RequestUtil.getRequest();
		String jwt = resolveJwt(request.getHeader(AUTHORIZATION));
		authenticate(jwt);
	}

	private String resolveJwt(String token) {
		BearerUtil.validate(token);
		return BearerUtil.resolve(token);
	}

	private void authenticate(String jwt) {
		Authentication authentication = jwtParser.parseAuthentication(jwt);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
