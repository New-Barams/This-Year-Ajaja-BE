package me.ajaja.module.user.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.user.application.port.out.CheckExistUserPort;

@Repository
@RequiredArgsConstructor
class CheckExistUserAdapter implements CheckExistUserPort {
	private final UserJpaRepository userJpaRepository;

	@Override
	public boolean isExist(Long id) {
		return userJpaRepository.existsById(id);
	}
}
