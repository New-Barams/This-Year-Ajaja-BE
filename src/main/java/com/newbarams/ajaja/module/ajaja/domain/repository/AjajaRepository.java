package com.newbarams.ajaja.module.ajaja.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newbarams.ajaja.module.ajaja.domain.Ajaja;

public interface AjajaRepository extends JpaRepository<Ajaja, Long>, AjajaQueryRepository {
	Optional<Ajaja> findByTargetIdAndUserIdAndType(Long targetId, Long userId, Ajaja.Type type);
}
