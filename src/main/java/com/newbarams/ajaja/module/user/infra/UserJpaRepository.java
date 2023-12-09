package com.newbarams.ajaja.module.user.infra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findBySignUpEmail(String email);

	boolean existsById(Long id);
}
