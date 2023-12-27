package com.newbarams.ajaja.module.remind.infra;

import java.time.Instant;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

@SpringBootTest
@Transactional
class RemindQueryRepositoryImplTest extends MockTestSupport {
	@Autowired
	private RemindQueryRepositoryImpl remindQueryRepositoryImpl;
	private Plan plan;
	private RemindInfo info;
	private List<Message> messages;

	@BeforeEach
	void setUp() {
		info = sut.giveMeBuilder(RemindInfo.class)
			.set("remindTerm", 12)
			.set("remindTotalPeriod", 12)
			.sample();
		messages = sut.giveMe(Message.class, 1);
	}

	@Test
	@DisplayName("플랜 id에 맞는 리마인드 정보를 가져온다.")
	void findRemindInfoByPlanId_Success_WithNoException() {
		// given
		plan = sut.giveMeBuilder(Plan.class)
			.set("id", 1L)
			.set("month", 1)
			.set("userId", 2L)
			.set("content", new Content("plan", "plan"))
			.set("info", info)
			.set("isPublic", true)
			.set("icon", 1)
			.set("messages", messages)
			.set("createdAt", Instant.now())
			.sample();

		// when
		RemindResponse.RemindInfo reminds = remindQueryRepositoryImpl.findAllReminds(plan);

		// then
		Assertions.assertThat(reminds.messagesResponses().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("만약 플랜id에 맞는 리마인드 정보가 없으면 메세지 정보만 반환한다.")
	void findNoRemindInfoByPlanId_Success_WithNoException() {
		// given
		plan = sut.giveMeBuilder(Plan.class)
			.set("id", 2L)
			.set("info", info)
			.set("messages", messages)
			.sample();

		// when, then
		Assertions.assertThatNoException().isThrownBy(() ->
			remindQueryRepositoryImpl.findAllReminds(plan)
		);
	}
}
