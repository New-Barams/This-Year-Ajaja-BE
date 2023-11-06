package com.newbarams.ajaja.module.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.plan.domain.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
}
