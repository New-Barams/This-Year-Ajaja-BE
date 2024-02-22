package me.ajaja.module.user.application.port.out;

public interface DisablePlanPort {
	/**
	 * Disable Activate Plans
	 * @param userId
	 */
	void disable(Long userId);
}
