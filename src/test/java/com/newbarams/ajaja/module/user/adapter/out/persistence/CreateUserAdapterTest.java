package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.newbarams.ajaja.common.support.JpaTestSupport;
import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import com.newbarams.ajaja.module.user.mapper.UserMapper;
import com.newbarams.ajaja.module.user.mapper.UserMapperImpl;

@ContextConfiguration(classes = {CreateUserAdapter.class, UserMapperImpl.class})
class CreateUserAdapterTest extends JpaTestSupport {
	@Autowired
	private CreateUserAdapter createUserAdapter;
	@Autowired
	private UserJpaRepository userJpaRepository;
	@Autowired
	private UserMapper userMapper;

	@Test
	void create_Success() {
		// given
		String email = "ajaja@me.com";

		// when
		Long userId = createUserAdapter.create(email, 1L);

		// then
		assertThat(userId).isNotNull();

		UserEntity userEntity = userJpaRepository.findAll().get(0);
		assertThat(userEntity.getSignUpEmail()).isEqualTo(email);
		assertThat(userEntity.isDeleted()).isFalse();
	}
}
