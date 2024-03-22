package me.ajaja.module.footprint.adapter.out.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;
import me.ajaja.module.footprint.application.port.out.CreateFootprintPort;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.mapper.FootprintMapper;

@Repository
@Transactional
@RequiredArgsConstructor
public class CreateFootprintAdapter implements CreateFootprintPort {
	private final FootprintJpaRepository footprintJpaRepository;
	private final FootprintMapper footprintMapper;

	@Override
	public Long create(Footprint footprint) {
		FootprintEntity footprintEntity = footprintMapper.toEntity(footprint);
		return footprintJpaRepository.save(footprintEntity).getId();
	}
}
