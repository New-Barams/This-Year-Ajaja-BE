package com.newbarams.ajaja.module.plan.domain;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.newbarams.ajaja.common.support.MonkeySupport;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.plan.dto.PlanParam;

class PlanTest extends MonkeySupport {
	@Test
	void createPlan_Success() {
		sut.giveMeOne(Plan.class);
	}

	@Test
	@DisplayName("삭제가능한 기간일 경우 작성한 계획을 삭제할 수 있다.")
	void deletePlan_Success() {
		PlanStatus planStatus = new PlanStatus(true);

		Plan plan = sut.giveMeBuilder(Plan.class)
			.set("status", planStatus)
			.sample();

		plan.delete(plan.getUserId(), 1);

		assertThat(plan.getStatus().isDeleted()).isEqualTo(true);
	}

	@Test
	@DisplayName("삭제가능한 기간이 아닌 경우 계획을 삭제할 수 없다.")
	void deletePlan_Fail_By_Date() {
		PlanStatus planStatus = new PlanStatus(true);

		Plan plan = sut.giveMeBuilder(Plan.class)
			.set("status", planStatus)
			.sample();

		assertThatThrownBy(() -> plan.delete(plan.getUserId(), 12))
			.isInstanceOf(AjajaException.class)
			.hasMessage(INVALID_UPDATABLE_DATE.getMessage());
	}

	@Test
	@DisplayName("계획의 공개여부를 변경할 수 있다.")
	void updatePublicStatus_Success() {
		Plan plan = sut.giveMeOne(Plan.class);
		boolean isPublic = plan.getStatus().isPublic();

		plan.updatePublicStatus(plan.getUserId());
		assertThat(plan.getStatus().isPublic()).isEqualTo(!isPublic);
	}

	@Test
	@DisplayName("리마인드 알림 on/off 여부를 변경할 수 있다.")
	void updateRemindStatus_Success() {
		Plan plan = sut.giveMeOne(Plan.class);
		boolean canRemind = plan.getStatus().isCanRemind();

		plan.updateRemindStatus(plan.getUserId());
		assertThat(plan.getStatus().isCanRemind()).isEqualTo(!canRemind);
	}

	@Test
	@DisplayName("응원메시지 알림 on/off 여부를 변경할 수 있다.")
	void updateAjajaStatus_Success() {
		Plan plan = sut.giveMeOne(Plan.class);
		boolean canAjaja = plan.getStatus().isCanAjaja();

		plan.updateAjajaStatus(plan.getUserId());
		assertThat(plan.getStatus().isCanAjaja()).isEqualTo(!canAjaja);
	}

	@Test
	@DisplayName("수정가능한 기간일 경우 작성한 계획을 수정할 수 있다.")
	void updatePlan_Success_With_Updatable_Date() {
		Plan plan = sut.giveMeOne(Plan.class);

		assertThatNoException().isThrownBy(() ->
			plan.update(new PlanParam.Update(1, 1, plan.getUserId(), new Content("title", "des"),
				true, true))
		);
	}

	@Test
	@DisplayName("수정가능한 기간이 아닐 경우 작성한 계획을 수정할 수 없다.")
	void updatePlan_Fail_By_Not_Updatable_Date() {
		Plan plan = sut.giveMeOne(Plan.class);
		List<Message> messages = sut.giveMe(Message.class, 3);

		assertThatThrownBy(() ->
			plan.update(new PlanParam.Update(12, 1, plan.getUserId(), new Content("title", "des"),
				true, true)))
			.isInstanceOf(AjajaException.class)
			.hasMessage(INVALID_UPDATABLE_DATE.getMessage());
	}

	@Test
	@DisplayName("수정하고자 하는 내용이 수정 후의 내용과 같아야 한다.")
	void updatePlan_Success() {
		Plan plan = sut.giveMeOne(Plan.class);

		plan.update(new PlanParam.Update(1, 1, plan.getUserId(), new Content("title", "des"),
			true, false));

		assertThat(plan.getContent().getTitle()).isEqualTo("title");
		assertThat(plan.getStatus().isCanAjaja()).isEqualTo(false);
	}
}
