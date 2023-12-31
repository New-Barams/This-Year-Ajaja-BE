package com.newbarams.ajaja.module.user.application.port.out;

import com.newbarams.ajaja.module.user.domain.User;

public interface ApplyChangePort {
	/**
	 * Persist user changes on DB
	 * @param user target to apply changes
	 */
	void apply(User user);
}
