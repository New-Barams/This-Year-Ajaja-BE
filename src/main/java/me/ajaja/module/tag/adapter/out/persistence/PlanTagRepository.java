package me.ajaja.module.tag.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import me.ajaja.module.tag.adapter.out.persistence.model.PlanTag;

public interface PlanTagRepository extends JpaRepository<PlanTag, Long> {
	void deleteAllByPlanId(Long planId);
}