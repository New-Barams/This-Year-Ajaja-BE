package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.support.MonkeySupport;
import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;

@SpringBootTest
@Transactional
class CreateUserAdapterTest extends MonkeySupport {
	@Autowired
	private CreateUserAdapter createUserAdapter;

	@Autowired
	private UserJpaRepository userJpaRepository;

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
