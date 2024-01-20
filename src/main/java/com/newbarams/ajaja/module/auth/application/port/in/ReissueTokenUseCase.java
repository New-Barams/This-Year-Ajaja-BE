package com.newbarams.ajaja.module.auth.application.port.in;

import com.newbarams.ajaja.module.auth.dto.AuthResponse;

public interface ReissueTokenUseCase {
	/**
	 * Reissue access token and refresh token if should.
	 * Only reissue refresh token if it will expire within 3 days.
	 * @return response with access token and refresh token
	 */
	AuthResponse.Token reissue(String accessToken, String refreshToken);
}
