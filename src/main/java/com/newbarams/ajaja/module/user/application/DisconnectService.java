package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;

@Service
public interface DisconnectService {
	void disconnect(String authorizationCode, String redirectUri);
}
