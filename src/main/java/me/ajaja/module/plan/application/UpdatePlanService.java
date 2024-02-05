package me.ajaja.module.plan.application;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.plan.application.port.in.UpdatePlanUseCase;
import me.ajaja.module.plan.application.port.out.FindPlanDetailPort;
import me.ajaja.module.plan.application.port.out.FindPlanPort;
import me.ajaja.module.plan.application.port.out.SavePlanPort;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.tag.application.UpdatePlanTagService;

@Service
@Transactional
@RequiredArgsConstructor
class UpdatePlanService implements UpdatePlanUseCase {
	private final SavePlanPort savePlanPort;
	private final FindPlanPort findPlanPort;
	private final FindPlanDetailPort findPlanDetailPort;
	private final UpdatePlanTagService updatePlanTagService;  // todo : 삭제 예정
	private final PlanMapper planMapper;

	@Override
	public PlanResponse.Detail update(Long id, Long userId, PlanRequest.Update request, int month) {
		Plan plan = loadPlanOrElseThrow(id);
		updatePlanTagService.update(id, request.getTags());

		plan.update(planMapper.toParam(userId, request, month));

		savePlanPort.save(plan);

		return loadByIdAndOptionalUser(userId, id);
	}

	private Plan loadPlanOrElseThrow(Long id) {
		return findPlanPort.findById(id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_PLAN));
	}

	private PlanResponse.Detail loadByIdAndOptionalUser(Long userId, Long id) {
		return findPlanDetailPort.findPlanDetailByIdAndOptionalUser(userId, id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_PLAN));
	}
}
