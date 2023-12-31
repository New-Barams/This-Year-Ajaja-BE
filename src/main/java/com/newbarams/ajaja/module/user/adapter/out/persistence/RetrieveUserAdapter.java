package com.newbarams.ajaja.module.user.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.user.application.port.out.RetrieveUserPort;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class RetrieveUserAdapter implements RetrieveUserPort {
	private final UserJpaRepository userJpaRepository;
	private final UserMapper userMapper;

	@Override
	public Optional<User> load(Long id) {
		return userJpaRepository.findById(id)
			.map(userMapper::toDomain);
	}
}
