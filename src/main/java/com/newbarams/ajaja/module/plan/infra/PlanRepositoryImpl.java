package com.newbarams.ajaja.module.plan.infra;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlanRepositoryImpl implements PlanRepository {
	private final PlanJpaRepository planJpaRepository;
	private final PlanMapper planMapper;

	@Override
	public Plan save(Plan plan) {
		PlanEntity planEntity = planMapper.toEntity(plan);
		PlanEntity saved = planJpaRepository.save(planEntity);

		return planMapper.toDomain(saved);
	}

	@Override
	public Optional<Plan> findById(Long id) {
		return planJpaRepository.findById(id)
			.map(planMapper::toDomain);
	}

	@Override
	public List<Plan> saveAll(List<Plan> plans) {
		return plans.stream()
			.map(this::save)
			.toList();
	}
}
