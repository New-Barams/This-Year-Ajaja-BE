package com.newbarams.ajaja.module.user.application.port.in;

import com.newbarams.ajaja.module.user.dto.UserResponse;

public interface LoginUseCase {
	UserResponse.Token login(String authorizationCode, String redirectUri);
}
