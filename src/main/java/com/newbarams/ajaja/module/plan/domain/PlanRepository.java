package com.newbarams.ajaja.module.plan.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository {
	Plan save(Plan plan);

	Optional<Plan> findById(Long id);

	List<Plan> saveAll(List<Plan> plans);
}
