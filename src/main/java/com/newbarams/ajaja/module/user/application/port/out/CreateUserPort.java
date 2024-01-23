package com.newbarams.ajaja.module.user.application.port.out;

import com.newbarams.ajaja.module.user.domain.User;

public interface CreateUserPort {
	/**
	 * Persist user with given data information
	 * @param user target to persist
	 * @return Identifier of user
	 */
	Long create(User user);
}
