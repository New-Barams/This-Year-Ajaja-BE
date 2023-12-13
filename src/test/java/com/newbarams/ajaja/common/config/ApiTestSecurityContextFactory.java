package com.newbarams.ajaja.common.config;

import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.global.security.common.UserAdapter;

public class ApiTestSecurityContextFactory implements WithSecurityContextFactory<ApiTest> {
	private static final UserDetails user = new UserAdapter(1L, "ajaja@me.com");

	@Override
	public SecurityContext createSecurityContext(ApiTest annotation) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(dummyAuthentication());
		return context;
	}

	private Authentication dummyAuthentication() {
		return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
	}
}
