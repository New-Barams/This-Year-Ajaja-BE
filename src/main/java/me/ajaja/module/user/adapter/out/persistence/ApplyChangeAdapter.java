package me.ajaja.module.user.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import me.ajaja.module.user.application.port.out.ApplyChangePort;
import me.ajaja.module.user.domain.User;
import me.ajaja.module.user.mapper.UserMapper;

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
