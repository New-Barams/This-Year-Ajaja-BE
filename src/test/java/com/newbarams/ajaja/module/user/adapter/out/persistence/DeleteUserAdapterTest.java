package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.newbarams.ajaja.common.support.JpaTestSupport;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.infra.UserEntity;
import com.newbarams.ajaja.module.user.infra.UserJpaRepository;
import com.newbarams.ajaja.module.user.mapper.UserMapper;
import com.newbarams.ajaja.module.user.mapper.UserMapperImpl;

@ContextConfiguration(classes = {DeleteUserAdapter.class, UserMapperImpl.class})
class DeleteUserAdapterTest extends JpaTestSupport {
	@Autowired
	private DeleteUserAdapter deleteUserAdapter;
	@Autowired
	private UserJpaRepository userJpaRepository;
	@Autowired
	private UserMapper userMapper;

	@Test
	void delete_Success() {
		// given
		User user = User.init("ajaja@me.com", sut.giveMeOne(Long.class));
		UserEntity entity = userJpaRepository.save(userMapper.toEntity(user));

		// when
		deleteUserAdapter.delete(entity.getId());

		// then
		List<UserEntity> entities = userJpaRepository.findAll();
		assertThat(entities).isEmpty();
	}
}
