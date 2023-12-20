package com.newbarams.ajaja.module.auth.application.port.in;

import com.newbarams.ajaja.module.auth.dto.AuthResponse;

public interface ReissueTokenUseCase {
	AuthResponse.Token reissue(String accessToken, String refreshToken);
}
