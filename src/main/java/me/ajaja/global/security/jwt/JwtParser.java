package me.ajaja.global.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.security.common.CustomUserDetailsService;

@Component
@RequiredArgsConstructor
public class JwtParser {
	private final CustomUserDetailsService userDetailService;
	private final JwtSecretProvider secretProvider;
	private final TokenCache tokenCache;
	private final RawParser rawParser;

	public Authentication parseAuthentication(String jwt) {
		Long userId = parseId(jwt);
		UserDetails user = userDetailService.loadUserByUsername(String.valueOf(userId));
		return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}

	public Long parseId(String jwt) {
		return rawParser.parseClaimWithType(jwt, secretProvider.getSignature(), Long.class);
	}

	/**
	 * If either of tokens are valid parse ID. <br>
	 * Valid access Token means that refresh token is also valid, so don't need to validate refresh token.
	 * However, if only refresh token is valid should validate whether user logged in before.
	 * @author hejow
	 */
	public Long parseIdIfReissueable(String accessToken, String refreshToken) {
		return rawParser.tryParse(accessToken) ? parseId(accessToken) : parseIdIfHistoryExists(refreshToken);
	}

	private Long parseIdIfHistoryExists(String refreshToken) {
		Long userId = parseId(refreshToken);
		tokenCache.validateHistory(secretProvider.cacheKey(userId), refreshToken);
		return userId;
	}
}
