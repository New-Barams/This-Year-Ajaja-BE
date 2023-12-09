package com.newbarams.ajaja.module.user.infra;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;
import com.newbarams.ajaja.module.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;
	private final UserMapper userMapper;

	@Override
	public User save(User user) {
		UserEntity entity = userJpaRepository.save(userMapper.toEntity(user));
		return userMapper.toDomain(entity);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userJpaRepository.findBySignUpEmail(email)
			.map(userMapper::toDomain);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userJpaRepository.findById(id)
			.map(userMapper::toDomain);
	}

	@Override
	public boolean existsById(Long id) {
		return userJpaRepository.existsById(id);
	}
}
