package me.ajaja.module.ajaja.infra;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaRepository;
import me.ajaja.module.ajaja.mapper.AjajaMapper;

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
