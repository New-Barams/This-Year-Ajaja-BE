package me.ajaja.module.tag.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanTagRepository extends JpaRepository<PlanTag, Long> {
	void deleteAllByPlanId(Long planId);
}
