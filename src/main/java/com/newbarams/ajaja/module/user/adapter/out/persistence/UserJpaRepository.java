package com.newbarams.ajaja.module.user.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findBySignUpEmail(String email);

	boolean existsById(Long id);
}
