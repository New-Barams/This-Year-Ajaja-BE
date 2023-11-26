package com.newbarams.ajaja.module.user.application.model;

import com.newbarams.ajaja.module.user.domain.OauthInfo;

/**
 * Oauth2 Interface
 */
public interface Profile {
	OauthInfo getInfo();

	String getEmail();
}
