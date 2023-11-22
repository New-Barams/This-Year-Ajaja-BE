package com.newbarams.ajaja.module.plan.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newbarams.ajaja.module.plan.domain.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {
	int countByUserId(Long userId);
}
