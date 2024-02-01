package me.ajaja.module.user.application.port.out;

public interface CheckExistUserPort {
	/**
	 * Check if user exists with input
	 * @param id target to check
	 * @return if user exist return TRUE if else FALSE
	 */
	boolean isExist(Long id);
}
