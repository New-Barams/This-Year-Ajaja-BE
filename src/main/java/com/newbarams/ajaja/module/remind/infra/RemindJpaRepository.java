package com.newbarams.ajaja.module.remind.infra;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RemindJpaRepository extends JpaRepository<RemindEntity, Long> {
}
