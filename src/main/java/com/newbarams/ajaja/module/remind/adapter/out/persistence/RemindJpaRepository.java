package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newbarams.ajaja.module.remind.adapter.out.persistence.model.RemindEntity;

interface RemindJpaRepository extends JpaRepository<RemindEntity, Long> {
}
