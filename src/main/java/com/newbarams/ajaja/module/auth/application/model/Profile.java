package com.newbarams.ajaja.module.auth.application.model;

/**
 * Interface that has user profile information from provider.
 * @author hejow
 */
public interface Profile {
	Long getOauthId();

	String getContact();

	String getEmail();
}
