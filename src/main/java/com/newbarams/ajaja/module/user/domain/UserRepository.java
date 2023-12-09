package com.newbarams.ajaja.module.user.domain;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
	User save(User user);

	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);

	boolean existsById(Long id);
}
