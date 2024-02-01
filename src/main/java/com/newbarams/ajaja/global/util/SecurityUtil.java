package com.newbarams.ajaja.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.newbarams.ajaja.global.security.common.UserAdapter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

	public static Long getId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserAdapter userAdapter = (UserAdapter)authentication.getPrincipal();
		return userAdapter.id();
	}
}
