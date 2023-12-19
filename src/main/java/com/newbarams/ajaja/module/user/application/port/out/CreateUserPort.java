package com.newbarams.ajaja.module.user.application.port.out;

import com.newbarams.ajaja.module.user.domain.User;

public interface CreateUserPort {
	/**
	 * Persist user with given data information
	 * @param user
	 * @return Identifier of user
	 */
	Long create(User user); // todo: User dependency on adapter
}
