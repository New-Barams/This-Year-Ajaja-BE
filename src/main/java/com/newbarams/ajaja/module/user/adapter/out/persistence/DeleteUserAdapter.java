package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.user.application.port.out.DeleteUserPort;
import com.newbarams.ajaja.module.user.infra.UserEntity;
import com.newbarams.ajaja.module.user.infra.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class DeleteUserAdapter implements DeleteUserPort {
	private final UserJpaRepository userJpaRepository;

	@Override
	public void delete(Long id) {
		UserEntity userEntity = userJpaRepository.findById(id)
			.orElseThrow(() -> AjajaException.withId(id, USER_NOT_FOUND));
		userEntity.delete();
	}
}
