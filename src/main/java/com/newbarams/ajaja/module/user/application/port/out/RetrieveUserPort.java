package com.newbarams.ajaja.module.user.application.port.out;

import java.util.Optional;

import com.newbarams.ajaja.module.user.domain.User;

public interface RetrieveUserPort {
	/**
	 * Find user domain from DB
	 * @param id
	 * @return User optional which is nullable
	 */
	Optional<User> load(Long id);
}
