package com.newbarams.ajaja.module.user.application.port.out;

public interface CreateUserPort {
	/**
	 * Persist user with given data information
	 * @param email Email that Oauth Server provide
	 * @param oauthId ID that Oauth Server provide
	 * @return Identifier of user
	 */
	Long create(String email, Long oauthId);
}
