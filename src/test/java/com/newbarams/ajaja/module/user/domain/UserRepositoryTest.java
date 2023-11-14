package com.newbarams.ajaja.module.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.newbarams.ajaja.common.JpaTestSupport;

class UserRepositoryTest extends JpaTestSupport {
	@Autowired
	private UserRepository userRepository;

	private User user;

	@BeforeEach
	void setup() {
		Email email = new Email("gmlwh124@naver.com");
		user = monkey.giveMeBuilder(User.class)
			.set("email", email)
			.set("isDeleted", false)
			.sample();
	}

	@Test
	@DisplayName("회원탈퇴되지 않은 유저를 조회하면 정상적으로 조회되어야 한다.")
	void existById_Success_ByNotDeletedUser() {
		// given
		User saved = userRepository.save(user);

		// when
		boolean shouldBeTrue = userRepository.existsById(saved.getId());

		// then
		assertThat(shouldBeTrue).isTrue();
	}

	@Test
	@DisplayName("회원탈퇴한 유저를 조회하면 아무것도 조회되지 않아야 한다.")
	void existById_Fail_ByDeletedUser() {
		// given
		user.delete();
		User saved = userRepository.save(user);

		// when
		boolean shouldBeFalse = userRepository.existsById(saved.getId());

		// then
		assertThat(shouldBeFalse).isFalse();
	}
}
