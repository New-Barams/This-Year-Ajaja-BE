package com.newbarams.ajaja.module.user.application.port.in;

public interface WithdrawUseCase {
	/**
	 * Withdraw user and disconnect from oauth server
	 * @param userId
	 * @param oauthId
	 */
	void withdraw(Long userId, Long oauthId);
}
