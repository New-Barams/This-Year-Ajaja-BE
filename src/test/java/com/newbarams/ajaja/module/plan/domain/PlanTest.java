package com.newbarams.ajaja.module.plan.domain;

import static com.newbarams.ajaja.module.plan.exception.ErrorMessage.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.newbarams.ajaja.module.tag.domain.Tag;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class PlanTest {
	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.defaultNotNull(true)
		.build();

	@Test
	void createPlan_Success() {
		fixtureMonkey.giveMeOne(Plan.class);
	}

	@Test
	@DisplayName("삭제가능한 기간일 경우 작성한 계획을 삭제할 수 있다.")
	void deletePlan_Success() {
		PlanStatus planStatus = new PlanStatus(true);

		Plan plan = fixtureMonkey.giveMeBuilder(Plan.class)
			.set("status", planStatus)
			.sample();

		plan.delete(plan.getUserId(), "Thu JAN 09 2023");

		assertThat(plan.getStatus().isDeleted()).isEqualTo(true);
	}

	@Test
	@DisplayName("삭제가능한 기간이 아닌 경우 계획을 삭제할 수 없다.")
	void deletePlan_Fail_By_Date() {
		PlanStatus planStatus = new PlanStatus(true);

		Plan plan = fixtureMonkey.giveMeBuilder(Plan.class)
			.set("status", planStatus)
			.sample();

		assertThatThrownBy(() -> plan.delete(plan.getUserId(), "Thu NOV 09 2023"))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage(INVALID_UPDATABLE_DATE.getMessage());
	}

	@Test
	@DisplayName("계획의 공개여부를 변경할 수 있다.")
	void updatePublicStatus_Success() {
		Plan plan = fixtureMonkey.giveMeOne(Plan.class);
		boolean isPublic = plan.getStatus().isPublic();

		plan.updatePublicStatus(plan.getUserId());
		assertThat(plan.getStatus().isPublic()).isEqualTo(!isPublic);
	}

	@Test
	@DisplayName("리마인드 알림 on/off 여부를 변경할 수 있다.")
	void updateRemindStatus_Success() {
		Plan plan = fixtureMonkey.giveMeOne(Plan.class);
		boolean canRemind = plan.getStatus().isCanRemind();

		plan.updateRemindStatus(plan.getUserId());
		assertThat(plan.getStatus().isCanRemind()).isEqualTo(!canRemind);
	}

	@Test
	@DisplayName("응원메시지 알림 on/off 여부를 변경할 수 있다.")
	void updateAjajaStatus_Success() {
		Plan plan = fixtureMonkey.giveMeOne(Plan.class);
		boolean canAjaja = plan.getStatus().isCanAjaja();

		plan.updateAjajaStatus(plan.getUserId());
		assertThat(plan.getStatus().isCanAjaja()).isEqualTo(!canAjaja);
	}

	@Test
	@DisplayName("수정가능한 기간일 경우 작성한 계획을 수정할 수 있다.")
	void updatePlan_Success_With_Updatable_Date() {
		Plan plan = fixtureMonkey.giveMeOne(Plan.class);
		List<Message> messages = fixtureMonkey.giveMe(Message.class, 3);

		assertThatNoException().isThrownBy(() ->
			plan.update(plan.getUserId(), "Thu JAN 09 2023", "title", "des", 12, 3, 1,
				"EVENING", true, true, true, messages)
		);
	}

	@Test
	@DisplayName("수정가능한 기간이 아닐 경우 작성한 계획을 수정할 수 없다.")
	void updatePlan_Fail_By_Not_Updatable_Date() {
		Plan plan = fixtureMonkey.giveMeOne(Plan.class);
		List<Message> messages = fixtureMonkey.giveMe(Message.class, 3);

		assertThatThrownBy(() -> plan.update(plan.getUserId(), "Thu DEC 09 2023", "title", "des", 12, 3,
			1, "EVENING", true, true, true, messages))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage(INVALID_UPDATABLE_DATE.getMessage());
	}

	@Test
	@DisplayName("수정하고자 하는 내용이 수정 후의 내용과 같아야 한다.")
	void updatePlan_Success() {
		Plan plan = fixtureMonkey.giveMeOne(Plan.class);
		List<Message> messages = fixtureMonkey.giveMe(Message.class, 3);

		plan.update(plan.getUserId(), "Thu JAN 09 2023", "title", "des", 12, 3, 1,
			"EVENING", true, false, true, messages);

		assertThat(plan.getContent().getTitle()).isEqualTo("title");
		assertThat(plan.getInfo().getRemindDate()).isEqualTo(1);
		assertThat(plan.getStatus().isCanRemind()).isEqualTo(false);
	}
}
