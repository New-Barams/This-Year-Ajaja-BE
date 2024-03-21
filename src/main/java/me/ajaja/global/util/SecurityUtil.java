package me.ajaja.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.common.UserAdapter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

	/**
	 * Get user ID from SecurityContext. To use this utility authentication should be done.
	 * @see Authorize
	 */
	public static Long getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserAdapter userAdapter = (UserAdapter)authentication.getPrincipal();
		return userAdapter.id();
	}
}
