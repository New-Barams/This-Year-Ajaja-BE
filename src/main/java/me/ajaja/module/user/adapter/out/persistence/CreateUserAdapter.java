package me.ajaja.module.user.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import me.ajaja.module.user.application.port.out.CreateUserPort;
import me.ajaja.module.user.domain.User;
import me.ajaja.module.user.mapper.UserMapper;

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
