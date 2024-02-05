package me.ajaja.module.plan.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import me.ajaja.module.plan.adapter.out.persistence.model.PlanEntity;

interface PlanJpaRepository extends JpaRepository<PlanEntity, Long> {
}
