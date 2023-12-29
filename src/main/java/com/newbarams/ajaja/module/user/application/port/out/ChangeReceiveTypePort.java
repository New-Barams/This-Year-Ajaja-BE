package com.newbarams.ajaja.module.user.application.port.out;

public interface ChangeReceiveTypePort {
	/**
	 * Change remind receive type of user
	 * @param id target user's id
	 * @param receiveType to be change
	 */
	void change(Long id, String receiveType);
}
