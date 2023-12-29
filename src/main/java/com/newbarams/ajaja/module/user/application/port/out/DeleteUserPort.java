package com.newbarams.ajaja.module.user.application.port.out;

public interface DeleteUserPort {
	/**
	 * Soft delete target user
	 * @param id target user's id
	 */
	void delete(Long id);
}
