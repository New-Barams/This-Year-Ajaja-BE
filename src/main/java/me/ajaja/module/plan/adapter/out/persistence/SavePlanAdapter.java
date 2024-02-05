package me.ajaja.module.plan.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.adapter.out.persistence.model.PlanEntity;
import me.ajaja.module.plan.application.port.out.SavePlanPort;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.mapper.PlanMapper;

@Repository
@RequiredArgsConstructor
class SavePlanAdapter implements SavePlanPort {
	private final PlanJpaRepository planJpaRepository;
	private final PlanMapper planMapper;

	@Override
	public Plan save(Plan plan) {
		PlanEntity planEntity = planMapper.toEntity(plan);
		PlanEntity saved = planJpaRepository.save(planEntity);

		return planMapper.toDomain(saved);
	}

	@Override
	public List<Plan> saveAll(List<Plan> plans) {
		return plans.stream()
			.map(this::save)
			.toList();
	}
}
