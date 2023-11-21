package com.newbarams.ajaja.module.user.domain.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.dto.UserResponse;

@SpringBootTest
@Transactional
class UserQueryRepositoryTest extends MockTestSupport {
	@Autowired
	private UserQueryRepository userQueryRepository;

	@Autowired
	private UserRepository userRepository;

	private User user;

	@BeforeEach
	void setup() {
		Email email = new Email("gmlwh124@naver.com");
		user = userRepository.save(monkey.giveMeBuilder(User.class)
			.set("email", email)
			.set("isDeleted", false)
			.sample());
	}

	@Test
	@DisplayName("사용자의 정보를 불러오면 올바른 응답값으로 가져올 수 있어야 한다.")
	void findUserInfoById_Success() {
		// given
		Long id = user.getId();

		// when
		UserResponse.MyPage result = userQueryRepository.findUserInfoById(id);

		// then
		assertThat(result).isNotNull();
		assertThat(result.defaultEmail()).isEqualTo(user.getEmail().getEmail());
		assertThat(result.remindEmail()).isEqualTo(user.getEmail().getRemindEmail());
		assertThat(result.isEmailVerified()).isEqualTo(user.getEmail().isVerified());
		assertThat(result.receiveType()).isLowerCase();
	}

	@Test
	@DisplayName("존재하지 않는 사용자는 null을 리턴한다.")
	void findUserInfoById_Fail_ByNotExists() {
		// given
		Long userId = -1L;

		// when
		UserResponse.MyPage result = userQueryRepository.findUserInfoById(userId);

		// then
		assertThat(result).isNull();
	}
}
