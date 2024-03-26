package me.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.mapper.MessageMapper;
import me.ajaja.module.remind.application.port.in.UpdateRemindInfoUseCase;
import me.ajaja.module.remind.application.port.out.FindTargetPort;
import me.ajaja.module.remind.application.port.out.SaveTargetPort;

@Service
@Transactional
@RequiredArgsConstructor
class UpdateRemindInfoService implements UpdateRemindInfoUseCase {
	private final FindTargetPort findTargetPort;
	private final SaveTargetPort saveTargetPort;
	private final MessageMapper mapper;

	public void update(Long userId, Long planId, PlanRequest.UpdateRemind request) {
		Plan plan = findTargetPort.findByUserIdAndPlanId(userId, planId);
		plan.updateRemind(mapper.toDomain(request), mapper.toDomain(request.getMessages()));
		saveTargetPort.update(plan);
	}
}
