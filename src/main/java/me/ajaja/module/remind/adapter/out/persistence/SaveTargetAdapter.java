package me.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.adapter.out.persistence.PlanJpaRepository;
import me.ajaja.module.plan.adapter.out.persistence.model.PlanEntity;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.remind.application.port.out.SaveTargetPort;

@Repository
@RequiredArgsConstructor
public class SaveTargetAdapter implements SaveTargetPort {
	private final PlanJpaRepository planJpaRepository;
	private final PlanMapper planMapper;

	@Override
	public void update(Plan plan) {
		PlanEntity planEntity = planMapper.toEntity(plan);
		planJpaRepository.save(planEntity);
	}
}
