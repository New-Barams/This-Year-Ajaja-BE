package com.newbarams.ajaja.module.remind.infra;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.MonkeySupport;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.repository.RemindQueryRepository;
import com.newbarams.ajaja.module.remind.domain.repository.RemindRepository;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

@SpringBootTest
@Transactional
class RemindQueryRepositoryTest extends MonkeySupport {
	@Autowired
	private RemindQueryRepository remindQueryRepository;
	@Autowired
	private RemindRepository remindRepository;
	private Remind remind;
	private Plan plan;
	private Feedback feedback;

	@BeforeEach
	void setUp() {
		plan = monkey.giveMeBuilder(Plan.class)
			.set("id", 1L)
			.sample();
		remind = remindRepository.save(monkey.giveMeBuilder(Remind.class)
			.set("planId", plan.getId())
			.set("remindType", Remind.Type.PLAN)
			.set("isDeleted", false)
			.sample());
		feedback = monkey.giveMeBuilder(Feedback.class)
			.set("planId", 1L)
			.sample();
	}

	@Test
	@DisplayName("만약 플랜id에 맞는 리마인드 정보가 없으면 빈 리스트를 반환한다.")
	void findNoRemindInfoByPlanId_Success_WithNoException() {
		// given
		plan = monkey.giveMeBuilder(Plan.class)
			.set("id", 2L)
			.sample();

		// when
		RemindResponse.CommonResponse reminds = remindQueryRepository.findAllRemindByPlanId(plan, List.of(feedback));

		// then
		Assertions.assertThat(reminds.sentRemindResponses().size()).isZero();
	}
}
