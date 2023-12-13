package com.newbarams.ajaja.module.user.application.port.in;

import com.newbarams.ajaja.module.user.dto.UserResponse;

public interface ReissueTokenUseCase {
	UserResponse.Token reissue(String accessToken, String refreshToken);
}
