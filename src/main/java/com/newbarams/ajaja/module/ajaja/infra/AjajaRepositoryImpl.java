package com.newbarams.ajaja.module.ajaja.infra;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.ajaja.domain.Ajaja;
import com.newbarams.ajaja.module.ajaja.domain.AjajaRepository;
import com.newbarams.ajaja.module.ajaja.mapper.AjajaMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AjajaRepositoryImpl implements AjajaRepository {
	private final AjajaJpaRepository ajajaJpaRepository;
	private final AjajaMapper ajajaMapper;

	@Override
	public void save(Ajaja ajaja) {
		ajajaJpaRepository.save(ajajaMapper.toEntity(ajaja));
	}

	@Override
	public Optional<Ajaja> findByTargetIdAndUserIdAndType(Long targetId, Long userId, String type) {
		return ajajaJpaRepository.findByTargetIdAndUserIdAndType(targetId, userId, type)
			.map(ajajaMapper::toDomain);
	}
}
