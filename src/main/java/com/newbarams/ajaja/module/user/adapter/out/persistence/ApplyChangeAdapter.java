package com.newbarams.ajaja.module.user.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.user.application.port.out.ApplyChangePort;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.infra.UserEntity;
import com.newbarams.ajaja.module.user.infra.UserJpaRepository;
import com.newbarams.ajaja.module.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class ApplyChangeAdapter implements ApplyChangePort {
	private final UserJpaRepository userJpaRepository;
	private final UserMapper userMapper;

	@Override
	public void apply(User user) { // todo: user dependency
		UserEntity userEntity = userMapper.toEntity(user);
		userJpaRepository.save(userEntity);
	}
}
