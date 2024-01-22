package com.newbarams.ajaja.module.user.application.port.out;

import java.util.Optional;

import com.newbarams.ajaja.module.user.domain.User;

public interface RetrieveUserPort {
	/**
	 * Find user which wrapped with Optional by identifier
	 * @param id
	 * @return Optional user
	 */
	Optional<User> loadById(Long id);

	/**
	 * Find user which wrapped with Optional by email <br>
	 * If "Optional" is empty it means that never Sign-up with given email.
	 * @param email Email that used on sign-up
	 * @return Optional user
	 */
	Optional<User> loadByEmail(String email);
}
