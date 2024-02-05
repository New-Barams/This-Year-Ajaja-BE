package me.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.remind.application.port.out.FindTargetPort;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindTargetAdapter implements FindTargetPort {
	@Override
	public Plan findByUserIdAndPlanId(Long userId, Long planId) {
		return null;
	}
}
