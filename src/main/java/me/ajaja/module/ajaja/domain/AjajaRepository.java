package me.ajaja.module.ajaja.domain;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface AjajaRepository {
	void save(Ajaja ajaja);

	Optional<Ajaja> findByTargetIdAndUserIdAndType(Long targetId, Long userId, String type);
}
