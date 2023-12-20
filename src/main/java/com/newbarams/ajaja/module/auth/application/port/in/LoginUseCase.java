package com.newbarams.ajaja.module.auth.application.port.in;

import com.newbarams.ajaja.module.auth.dto.AuthResponse;

public interface LoginUseCase {
	AuthResponse.Token login(String authorizationCode, String redirectUri);
}
