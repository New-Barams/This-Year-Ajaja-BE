package com.newbarams.ajaja.module.remind.infra;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.repository.RemindRepository;
import com.newbarams.ajaja.module.remind.dto.GetRemindInfo;

@SpringBootTest
@Transactional
class RemindQueryRepositoryTest extends MockTestSupport {
	@Autowired
	private RemindQueryRepository remindQueryRepository;
	@Autowired
	private RemindRepository remindRepository;
	private Remind remind;
	private Plan plan;
	private Feedback feedback;

	@BeforeEach
	void setUp() {
		remind = remindRepository.save(monkey.giveMeBuilder(Remind.class)
			.set("planId", 1L)
			.set("remindType", Remind.Type.PLAN)
			.set("isDeleted", false)
			.sample());
		feedback = monkey.giveMeBuilder(Feedback.class)
			.set("planId", 1L)
			.sample();
	}

	@Test
	void findRemindInfoByPlanId_Success_WithNoException() {
		// given
		plan = monkey.giveMeBuilder(Plan.class)
			.set("id", 1L)
			.sample();

		// when
		GetRemindInfo.CommonResponse reminds = remindQueryRepository.findAllRemindByPlanId(plan, List.of(feedback));

		// then
		Assertions.assertThat(reminds.sentRemindResponses().size()).isEqualTo(1);
	}

	@Test
	void findNoRemindInfoByPlanId_Success_WithNoException() {
		// given
		plan = monkey.giveMeBuilder(Plan.class)
			.set("id", 2L)
			.sample();

		// when
		GetRemindInfo.CommonResponse reminds = remindQueryRepository.findAllRemindByPlanId(plan, List.of(feedback));

		// then
		Assertions.assertThat(reminds.sentRemindResponses().size()).isZero();
	}
}
