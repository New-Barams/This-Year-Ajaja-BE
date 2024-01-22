package com.newbarams.ajaja.module.user.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import com.newbarams.ajaja.module.user.application.port.out.FindUserIdPort;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class FindUserIdAdapter implements FindUserIdPort {
	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<Long> findByEmail(String email) {
		return userJpaRepository.findBySignUpEmail(email)
			.map(UserEntity::getId);
	}
}
