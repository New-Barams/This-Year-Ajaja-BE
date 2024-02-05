package me.ajaja.module.plan.domain.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import me.ajaja.common.support.MonkeySupport;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.plan.application.port.out.FindAllPlansPort;
import me.ajaja.module.plan.application.port.out.FindPlanDetailPort;
import me.ajaja.module.plan.application.port.out.SavePlanPort;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.PlanStatus;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.remind.application.port.out.FindPlanRemindQuery;
import me.ajaja.module.remind.application.port.out.FindRemindablePlanPort;
import me.ajaja.module.user.adapter.out.persistence.UserJpaRepository;
import me.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import me.ajaja.module.user.domain.User;
import me.ajaja.module.user.mapper.UserMapper;

@SpringBootTest
@Transactional
class PlanQueryRepositoryTest extends MonkeySupport {
	@Autowired
	private SavePlanPort savePlanPort;
	@Autowired
	private FindAllPlansPort findAllPlansPort;
	@Autowired
	private FindPlanDetailPort findPlanDetailPort;
	@Autowired
	private FindRemindablePlanPort findRemindablePlanPort;
	@Autowired
	private FindPlanRemindQuery findPlanRemindQuery;
	@Autowired
	private UserJpaRepository userRepository;
	@Autowired
	private UserMapper userMapper;

	private User user;
	private Plan plan;

	@BeforeEach
	void deleteAll() {
		UserEntity entity = userMapper.toEntity(User.init(1L, "+82 1012345678", "ajaja@me.com"));
		user = userMapper.toDomain(userRepository.save(entity));

		plan = sut.giveMeBuilder(Plan.class)
			.set("userId", this.user.getId())
			.set("status", new PlanStatus(true, true))
			.set("ajajas", Collections.EMPTY_LIST)
			.sample();
	}

	@Nested
	@DisplayName("findPlanDetailByIdAndOptionalUser 테스트")
	class FindPlanDetailByIdAndOptionalUserTest {
		@Test
		@DisplayName("사용자 ID를 입력하지 않고 상세 조회하면 작성자 정보에서 작성자 여부와 좋아요 여부는 false가 되어야 한다.")
		void findPlanDetailByIdAndOptionalUser_Success_WithUserIdNotEntered() { // no ajaja pressed query
			// given
			Plan save = savePlanPort.save(plan);

			// when
			Optional<PlanResponse.Detail> result =
				findPlanDetailPort.findPlanDetailByIdAndOptionalUser(null, save.getId());

			// then
			assertThat(result).isNotEmpty();

			PlanResponse.Detail detail = result.get();
			assertThat(detail.getWriter().getNickname()).isEqualTo(user.getNickname().getNickname());
			assertThat(detail.getWriter().isOwner()).isFalse();
			assertThat(detail.getWriter().isAjajaPressed()).isFalse();
			assertThat(detail.getId()).isEqualTo(save.getId());
			assertThat(detail.isPublic()).isEqualTo(save.getStatus().isPublic());
		}

		@Test
		@DisplayName("작성자의 ID로 상세 조회하면 작성자 정보에서 작성자 여부는 true가 되어야 한다.")
		void findPlanDetailByIdAndOptionalUser_Success_WithWriterUserId() {
			// given
			Plan save = savePlanPort.save(plan);

			// when
			Optional<PlanResponse.Detail> result =
				findPlanDetailPort.findPlanDetailByIdAndOptionalUser(user.getId(), save.getId());

			// then
			assertThat(result).isNotEmpty();

			PlanResponse.Detail detail = result.get();
			assertThat(detail.getWriter().getNickname()).isEqualTo(user.getNickname().getNickname());
			assertThat(detail.getWriter().isOwner()).isTrue();
			assertThat(detail.getId()).isEqualTo(save.getId());
			assertThat(detail.isPublic()).isEqualTo(save.getStatus().isPublic());
		}

		@Test
		@DisplayName("다른 사람의 id로 상세 조회하면 작성자 정보에서 작성자 여부와 좋아요 여부는 false가 되어야 한다.")
		void findPlanDetailByIdAndOptionalUser_Success_WithStrangerId() {
			// given
			Long strangerId = user.getId() + 1;
			Plan save = savePlanPort.save(plan);

			// when
			Optional<PlanResponse.Detail> result =
				findPlanDetailPort.findPlanDetailByIdAndOptionalUser(strangerId, save.getId());

			// then
			assertThat(result).isNotEmpty();

			PlanResponse.Detail detail = result.get();
			assertThat(detail.getWriter().isOwner()).isFalse();
			assertThat(detail.getWriter().isAjajaPressed()).isFalse();
			assertThat(detail.getId()).isEqualTo(save.getId());
			assertThat(detail.isPublic()).isEqualTo(save.getStatus().isPublic());
		}
	}

	@Test
	@DisplayName("올해의 계획들만 사용자의 아이디로 가져올 수 있어야 한다.")
	void findAllCurrentPlansByUserId_Success() {
		// given
		int expectedSize = 4;
		Long userId = sut.giveMeOne(Long.class);

		List<Plan> plans = sut.giveMeBuilder(Plan.class)
			.set("userId", userId)
			.set("status", new PlanStatus(true, true))
			.set("ajajas", Collections.EMPTY_LIST)
			.sampleList(expectedSize);

		savePlanPort.saveAll(plans);

		// when
		List<Plan> currentPlans = findAllPlansPort.findAllCurrentPlansByUserId(userId);

		// then
		assertThat(currentPlans).isNotEmpty().hasSize(expectedSize);
	}

	@Test
	@DisplayName("계획의 ID를 이용해 하나의 계획을 가져올 수 있다.")
	void findByUserIdAndPlanId_Success_WithNoException() {
		Plan savedPlan = savePlanPort.save(plan);

		Plan plan = findPlanRemindQuery.loadByUserIdAndPlanId(savedPlan.getUserId(), savedPlan.getId());
		assertThat(plan).isNotNull();
	}

	@Test
	@DisplayName("계획의 ID를 이용해 하나의 계획을 가져올 수 있다.")
	void findByUserIdAndPlanId_Fail_ByNotMatchPlanIdAndUserId() {
		Plan savedPlan = savePlanPort.save(plan);
		Long userId = savedPlan.getUserId() + 1L;

		assertThatException().isThrownBy(
			() -> findPlanRemindQuery.findByUserIdAndPlanId(userId, savedPlan.getId())
		);
	}

	@Test
	@DisplayName("최신순 or 인기순 정렬 시 지정된 개수만큼의 계획을 받는다.")
	void findAllByCursorAndSorting_Default_Success() {
		int pageSize = 3;

		List<Plan> plans = sut.giveMeBuilder(Plan.class)
			.set("userId", user.getId())
			.set("status", new PlanStatus(true, true))
			.set("ajajas", Collections.EMPTY_LIST)
			.sampleList(10);

		savePlanPort.saveAll(plans);

		PlanRequest.GetAll latestReq = new PlanRequest.GetAll("latest", true, null, null);
		PlanRequest.GetAll ajajaReq = new PlanRequest.GetAll("ajaja", true, null, null);

		List<PlanResponse.GetAll> latestRes = findAllPlansPort.findAllByCursorAndSorting(latestReq);
		List<PlanResponse.GetAll> ajajaRes = findAllPlansPort.findAllByCursorAndSorting(ajajaReq);

		assertThat(latestRes).hasSize(pageSize);
		assertThat(ajajaRes).hasSize(pageSize);
	}

	@Test
	@DisplayName("최신순 정렬 시 아이디 순으로 정렬된 계획들을 반환한다.")
	void findAllByCursorAndSorting_With_Latest_Condition_Success() {
		int pageSize = 3;

		List<Plan> plans = sut.giveMeBuilder(Plan.class)
			.set("userId", user.getId())
			.set("status", new PlanStatus(true, true))
			.set("ajajas", Collections.EMPTY_LIST)
			.sampleList(pageSize);

		savePlanPort.saveAll(plans);

		PlanRequest.GetAll latestReq = new PlanRequest.GetAll("latest", true, null, null);

		List<PlanResponse.GetAll> latestRes = findAllPlansPort.findAllByCursorAndSorting(latestReq);

		for (int i = 0; i < pageSize - 1; i++) {
			assertThat(latestRes.get(i).getId()).isGreaterThan(latestRes.get(i + 1).getId());
		}
	}

	@Test
	@DisplayName("해당 시간에 리마인드 가능한 계획들을 조회한다.")
	void findAllRemindablePlan_Success_WithNoException() {
		// when,then
		Assertions.assertThatNoException().isThrownBy(
			() -> findRemindablePlanPort.findAllRemindablePlan("MORNING", "EMAIL", TimeValue.now())
		);
	}

	@Test
	@DisplayName("계획 전체 조회 시 탈퇴한 회원의 계획도 조회되어야 한다.")
	void findAllByCursorAndSorting_Success_WithDeletedUser() {
		// given
		savePlanPort.save(plan);
		PlanRequest.GetAll latestReq = new PlanRequest.GetAll("latest", true, null, null);

		// when
		user.delete();
		userRepository.save(userMapper.toEntity(user));

		List<PlanResponse.GetAll> latestRes = findAllPlansPort.findAllByCursorAndSorting(latestReq);

		// then
		assertThat(latestRes).hasSize(1);
	}
}
