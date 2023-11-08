package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;

import com.newbarams.ajaja.module.user.auth.model.Profile;

@Service
public interface GetProfileService {
	/**
	 * @param accessToken Access Token that result of authorization from Authorization Server
	 * @return Profile from Resource Server
	 */
	Profile getProfile(String accessToken);
}
