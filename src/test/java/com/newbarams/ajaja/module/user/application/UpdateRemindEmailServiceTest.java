package com.newbarams.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.newbarams.ajaja.common.MonkeySupport;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;

@SpringBootTest
class UpdateRemindEmailServiceTest extends MonkeySupport {
	@Autowired
	private UpdateRemindEmailService updateRemindEmailService;

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void clear() {
		userRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("다른 리마인드 이메일이 입력되면 업데이트가 진행되어야 한다.")
	void updateIfDifferent_Updated_ByDifferentEmail() {
		// given
		String newRemindEmail = "hejow124@naver.com";
		Email email = new Email("gmlwh124@naver.com");

		User user = userRepository.save(monkey.giveMeBuilder(User.class)
			.set("email", email)
			.set("isDeleted", false)
			.sample());

		// when
		updateRemindEmailService.updateIfDifferent(user.getId(), newRemindEmail);

		// then
		User saved = userRepository.findAll().get(0);
		assertThat(saved.getEmail()).usingRecursiveComparison().isNotEqualTo(email);
	}

	@Test
	@DisplayName("같은 리마인드 이메일이 입력되면 업데이트를 진행하지 않아야 한다.")
	void updateIfDifferent_Passed_BySameEmail() {
		// given
		String input = "gmlwh124@naver.com";
		Email email = new Email(input);
		User user = userRepository.save(monkey.giveMeBuilder(User.class)
			.set("email", email)
			.set("isDeleted", false)
			.sample());

		// when
		updateRemindEmailService.updateIfDifferent(user.getId(), input);

		// then
		User saved = userRepository.findAll().get(0);
		assertThat(saved.getEmail()).usingRecursiveComparison().isEqualTo(email);
	}
}
