package com.newbarams.ajaja.module.user.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import com.newbarams.ajaja.module.user.application.port.out.CreateUserPort;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class CreateUserAdapter implements CreateUserPort {
	private final UserJpaRepository userJpaRepository;
	private final UserMapper userMapper;

	@Override
	public Long create(User user) {
		UserEntity userEntity = userMapper.toEntity(user);
		return userJpaRepository.save(userEntity).getId();
	}
}
