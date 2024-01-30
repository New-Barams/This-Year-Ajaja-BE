package com.newbarams.ajaja.global.security.jwt;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.security.common.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtParser {
	private final CustomUserDetailsService userDetailService;
	private final JwtSecretProvider jwtSecretProvider;
	private final RawParser rawParser;

	public Authentication parseAuthentication(String jwt) {
		Long userId = parseId(jwt);
		UserDetails user = userDetailService.loadUserByUsername(String.valueOf(userId));
		return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}

	public Long parseId(String jwt) {
		return getSpecificClaim(jwt, jwtSecretProvider.getSignature(), Long.class);
	}

	boolean isParsable(String jwt) {
		return rawParser.isParsable(jwt);
	}

	Date parseExpireIn(String refreshToken) {
		return getSpecificClaim(refreshToken, jwtSecretProvider.getDateKey(), Date.class);
	}

	private <T> T getSpecificClaim(String token, String key, Class<T> type) {
		return rawParser.parseClaim(token)
			.map(claims -> claims.get(key, type))
			.orElseThrow(() -> new AjajaException(INVALID_TOKEN));
	}
}
