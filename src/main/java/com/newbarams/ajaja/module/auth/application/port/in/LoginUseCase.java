package com.newbarams.ajaja.module.auth.application.port.in;

import com.newbarams.ajaja.module.auth.dto.AuthResponse;

public interface LoginUseCase {
	/**
	 * login by using Oauth
	 * @param authorizationCode authorization code that created by oauth provider
	 * @param redirectUri redirect uri after login success
	 * @return response with access token and refresh token
	 */
	AuthResponse.Token login(String authorizationCode, String redirectUri);
}
