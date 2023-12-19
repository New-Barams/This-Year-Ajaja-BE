package com.newbarams.ajaja.module.user.application.port.out;

import java.util.Optional;

public interface FindUserIdByEmailPort {
	/**
	 * Find user identifier which wrapped by Optional by email <br>
	 * If "Optional" is empty, it means that never Sign-up with given email.
	 * @param email Email that used on sign-up
	 * @return Optional identifier of user
	 */
	Optional<Long> findUserIdByEmail(String email);
}
