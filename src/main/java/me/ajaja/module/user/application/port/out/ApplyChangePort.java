package me.ajaja.module.user.application.port.out;

import me.ajaja.module.user.domain.User;

public interface ApplyChangePort {
	/**
	 * Persist user changes on DB
	 * @param user target to apply changes
	 */
	void apply(User user);
}
