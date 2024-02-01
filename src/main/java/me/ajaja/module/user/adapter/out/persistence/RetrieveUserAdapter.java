package me.ajaja.module.user.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.user.application.port.out.RetrieveUserPort;
import me.ajaja.module.user.domain.User;
import me.ajaja.module.user.mapper.UserMapper;

@Repository
@RequiredArgsConstructor
class RetrieveUserAdapter implements RetrieveUserPort {
	private final UserJpaRepository userJpaRepository;
	private final UserMapper userMapper;

	@Override
	public Optional<User> loadById(Long id) {
		return userJpaRepository.findById(id)
			.map(userMapper::toDomain);
	}

	@Override
	public Optional<User> loadByEmail(String email) {
		return userJpaRepository.findBySignUpEmail(email)
			.map(userMapper::toDomain);
	}
}
