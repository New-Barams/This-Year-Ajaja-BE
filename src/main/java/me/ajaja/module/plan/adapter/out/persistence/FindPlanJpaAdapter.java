package me.ajaja.module.plan.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.application.port.out.FindPlanPort;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.mapper.PlanMapper;

@Repository
@RequiredArgsConstructor
class FindPlanJpaAdapter implements FindPlanPort {
	private final PlanJpaRepository planJpaRepository;
	private final PlanMapper planMapper;

	@Override
	public Optional<Plan> findById(Long id) {
		return planJpaRepository.findById(id)
			.map(planMapper::toDomain);
	}
}
