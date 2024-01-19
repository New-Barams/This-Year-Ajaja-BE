package com.newbarams.ajaja.common.config;

import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.test.context.support.WithUserDetails;

import com.newbarams.ajaja.global.security.common.UserAdapter;

public class UserDetailsSecurityContextFactory implements WithSecurityContextFactory<WithUserDetails> {
	private static final UserDetails USER = new UserAdapter(1L, 1L);

	@Override
	public SecurityContext createSecurityContext(WithUserDetails annotation) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(dummyAuthentication());
		return context;
	}

	private Authentication dummyAuthentication() {
		return new UsernamePasswordAuthenticationToken(USER, null, Collections.emptyList());
	}
}
