package com.newbarams.ajaja.module.user.application.port.out;

public interface DisconnectOauthPort {
	/**
	 * Request disconnect Ajaja Application from the Oauth Provider
	 * @param oauthId Which Oauth Server provided when sign-up
	 */
	void disconnect(Long oauthId);
}
