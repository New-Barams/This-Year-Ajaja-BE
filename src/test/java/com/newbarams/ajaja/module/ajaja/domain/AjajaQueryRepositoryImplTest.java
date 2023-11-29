package com.newbarams.ajaja.module.ajaja.domain;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.MonkeySupport;
import com.newbarams.ajaja.module.ajaja.domain.repository.AjajaRepository;
import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.domain.repository.PlanRepository;
import com.newbarams.ajaja.module.remind.application.model.RemindableAjaja;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.repository.UserRepository;

@SpringBootTest
@Transactional
class AjajaQueryRepositoryImplTest extends MonkeySupport {
	@Autowired
	private AjajaRepository ajajaRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PlanRepository planRepository;

	@BeforeEach
	void setUp() {
		Email email = new Email("yamsang2002@naver.com");
		User user = userRepository.save(monkey.giveMeBuilder(User.class)
			.set("email", email)
			.set("isDeleted", false)
			.sample());

		RemindInfo remindInfo = monkey.giveMeOne(RemindInfo.class);
		Content content = monkey.giveMeOne(Content.class);

		Plan plan = new Plan(1, 1L, content, remindInfo, true, 1, Collections.EMPTY_LIST);
		planRepository.save(plan);

		ajajaRepository.saveAll(
			List.of(Ajaja.plan(plan.getId(), 1L), Ajaja.plan(plan.getId(), 1L))
		);
	}

	@Order(0)
	@Test
	@DisplayName("해당 유저가 받은 아자자 개수들을 가져온다.")
	void findAjajaByUpdatedAt_Success_WithNpException() {
		// when
		List<RemindableAjaja> remindableAjaja = ajajaRepository.findRemindableAjaja();
		System.out.println(ajajaRepository.findAll().size());

		// then
		Assertions.assertThat(remindableAjaja.size()).isEqualTo(1);
		Assertions.assertThat(remindableAjaja.get(0).count()).isEqualTo(2);
	}
}
