package com.newbarams.ajaja.module.user.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.user.application.port.out.FindUserIdByEmailPort;
import com.newbarams.ajaja.module.user.infra.UserEntity;
import com.newbarams.ajaja.module.user.infra.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class FindUserIdByEmailAdapter implements FindUserIdByEmailPort {
	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<Long> findUserIdByEmail(String email) {
		return userJpaRepository.findBySignUpEmail(email)
				.map(UserEntity::getId);
	}
}
