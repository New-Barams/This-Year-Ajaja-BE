package com.newbarams.ajaja.module.plan.repository;

import com.newbarams.ajaja.module.plan.domain.dto.PlanResponse;

public interface PlanRepositoryCustom {
	PlanResponse.GetOne findPlanById(Long id);
}
