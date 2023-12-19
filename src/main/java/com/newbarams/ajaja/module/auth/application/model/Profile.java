package com.newbarams.ajaja.module.auth.application.model;

/**
 * Get Profile Information only needed to issue Token OR sign-up
 * @author hejow
 */
public interface Profile {
	Long getOauthId();

	String getEmail();
}
