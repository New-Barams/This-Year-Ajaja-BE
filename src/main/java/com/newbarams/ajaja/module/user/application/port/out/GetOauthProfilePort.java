package com.newbarams.ajaja.module.user.application.port.out;

import com.newbarams.ajaja.module.user.application.model.Profile;

public interface GetOauthProfilePort {
	/**
	 * Get profile information from thr Oauth Provider
	 * @param accessToken Access Token that result of authorization from Authorization Server
	 * @return Profile from Resource Server
	 */
	Profile getProfile(String accessToken);
}
