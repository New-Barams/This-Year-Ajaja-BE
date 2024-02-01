package me.ajaja.module.remind.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.plan.infra.PlanQueryRepository;
import me.ajaja.module.remind.application.port.out.FindPlanInfoPort;

@Repository
@RequiredArgsConstructor
public class FindPlanInfoAdapter implements FindPlanInfoPort {
	private final PlanQueryRepository planQueryRepository;

	@Override
	public List<PlanResponse.PlanInfo> findAllPlanInfosByUserId(Long userId) {
		return planQueryRepository.findAllPlanByUserId(userId);
	}
}
