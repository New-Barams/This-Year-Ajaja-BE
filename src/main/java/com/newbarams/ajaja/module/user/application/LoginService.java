package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;

@Service
public interface LoginService {
	String login(String authorizationCode);
}
