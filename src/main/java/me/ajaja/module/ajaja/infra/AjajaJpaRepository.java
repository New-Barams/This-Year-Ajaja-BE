package me.ajaja.module.ajaja.infra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AjajaJpaRepository extends JpaRepository<AjajaEntity, Long> {
	Optional<AjajaEntity> findByTargetIdAndUserIdAndType(Long targetId, Long userId, String type);
}
