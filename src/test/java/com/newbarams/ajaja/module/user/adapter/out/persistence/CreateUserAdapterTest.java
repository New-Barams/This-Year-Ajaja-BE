package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.support.MonkeySupport;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.infra.UserEntity;
import com.newbarams.ajaja.module.user.infra.UserJpaRepository;

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
		User user = sut.giveMeBuilder(User.class)
				.set("email", new Email(email))
				.set("deleted", false)
				.sample();

		// when
		Long userId = createUserAdapter.create(user);

		// then
		assertThat(userId).isNotNull();

		UserEntity userEntity = userJpaRepository.findAll().get(0);
		assertThat(userEntity.getSignUpEmail()).isEqualTo(email);
		assertThat(userEntity.isDeleted()).isFalse();
	}
}
