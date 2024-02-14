package me.ajaja.module.footprint.adapter.out.persistence;

import static me.ajaja.global.exception.ErrorCode.NOT_FOUND_FOOTPRINT;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;
import me.ajaja.module.footprint.application.port.out.GetFootprintPort;
import me.ajaja.module.footprint.domain.FreeFootprint;
import me.ajaja.module.footprint.domain.KptFootprint;
import me.ajaja.module.footprint.mapper.FreeFootprintMapper;
import me.ajaja.module.footprint.mapper.KptFootprintMapper;

@Repository
@RequiredArgsConstructor
public class GetFootprintAdaptor implements GetFootprintPort {
	private final FootprintJpaRepository footprintJpaRepository;
	private final FreeFootprintMapper freeFootprintMapper;
	private final KptFootprintMapper kptFootprintMapper;

	@Override
	public FreeFootprint getFreeFootprint(Long id) {
		FootprintEntity footprintEntity = footprintJpaRepository.findById(id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_FOOTPRINT));

		return freeFootprintMapper.toDomain(footprintEntity);
	}

	@Override
	public KptFootprint getKptFootprint(Long id) {
		FootprintEntity footprintEntity = footprintJpaRepository.findById(id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_FOOTPRINT));

		return kptFootprintMapper.toDomain(footprintEntity);
	}
}
