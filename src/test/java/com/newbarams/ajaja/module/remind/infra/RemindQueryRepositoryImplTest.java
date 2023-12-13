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
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.RemindRepository;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

@SpringBootTest
@Transactional
class RemindQueryRepositoryImplTest extends MockTestSupport {
	@Autowired
	private RemindQueryRepositoryImpl remindQueryRepositoryImpl;
	@Autowired
	private RemindRepository remindRepository;

	private Remind remind;
	private Plan plan;
	private Feedback feedback;

	@BeforeEach
	void setUp() {
		remind = remindRepository.save(sut.giveMeBuilder(Remind.class)
			.set("planId", 1L)
			.set("type", Remind.Type.PLAN)
			.set("remindDate", Instant.now())
			.sample());

		feedback = sut.giveMeBuilder(Feedback.class)
			.set("planId", 1L)
			.sample();
	}

	@Test
	@DisplayName("플랜 id에 맞는 리마인드 정보를 가져온다.")
	void findRemindInfoByPlanId_Success_WithNoException() {
		// given
		RemindInfo info = sut.giveMeOne(RemindInfo.class);
		List<Message> messages = sut.giveMe(Message.class, 5);
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
		RemindResponse.CommonResponse reminds = remindQueryRepositoryImpl.findAllRemindByPlanId(plan,
			List.of(feedback));

		// then
		Assertions.assertThat(reminds.RemindResponses().size()).isEqualTo(1); // todo: 테스트 오류로 인한 임시 주석 처리
	}

	@Test
	@DisplayName("만약 플랜id에 맞는 리마인드 정보가 없으면 빈 리스트를 반환한다.")
	void findNoRemindInfoByPlanId_Success_WithNoException() {
		// given
		plan = sut.giveMeBuilder(Plan.class)
			.set("id", 2L)
			.sample();

		// when
		RemindResponse.CommonResponse reminds = remindQueryRepositoryImpl.findAllRemindByPlanId(plan,
			List.of(feedback));

		// then
		Assertions.assertThat(reminds.RemindResponses()).isEmpty();
	}
}
