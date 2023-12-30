package com.newbarams.ajaja.module.user.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.user.application.port.out.CheckExistUserPort;
import com.newbarams.ajaja.module.user.infra.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class CheckExistUserAdapter implements CheckExistUserPort {
	private final UserJpaRepository userJpaRepository;

	@Override
	public boolean isExist(Long id) {
		return userJpaRepository.existsById(id);
	}
}
