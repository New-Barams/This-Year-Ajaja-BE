package com.newbarams.ajaja.module.tag.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newbarams.ajaja.module.tag.domain.PlanTag;

public interface PlanTagRepository extends JpaRepository<PlanTag, Long> {
	void deleteAllByPlanId(Long planId);
}
