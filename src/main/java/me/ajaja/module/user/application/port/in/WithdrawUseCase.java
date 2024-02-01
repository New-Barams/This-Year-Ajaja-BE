package me.ajaja.module.user.application.port.in;

public interface WithdrawUseCase {
	/**
	 * Withdraw user and disconnect from oauth server
	 * @param userId
	 */
	void withdraw(Long userId);
}
