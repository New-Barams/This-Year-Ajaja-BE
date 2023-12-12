package com.newbarams.ajaja.module.user.application.service;

import com.newbarams.ajaja.module.user.application.model.Profile;

public interface GetProfileService {
	/**
	 * @param accessToken Access Token that result of authorization from Authorization Server
	 * @return Profile from Resource Server
	 */
	Profile getProfile(String accessToken);
}
