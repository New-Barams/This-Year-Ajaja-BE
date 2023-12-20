package com.newbarams.ajaja.module.user.application.port.out;

public interface SendCertificationPort {
	/**
	 * Send certification to request email
	 * @param email Where to send
	 * @param certification Six random generated Numbers
	 */
	void send(String email, String certification);
}
