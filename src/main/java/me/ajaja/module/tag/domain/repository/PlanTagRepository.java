package me.ajaja.module.tag.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.ajaja.module.tag.domain.PlanTag;

public interface PlanTagRepository extends JpaRepository<PlanTag, Long> {
	void deleteAllByPlanId(Long planId);
}
