package me.ajaja.module.footprint.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;
import me.ajaja.module.footprint.application.port.out.CreateFootprintPort;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FreeFootprint;
import me.ajaja.module.footprint.domain.KptFootprint;
import me.ajaja.module.footprint.mapper.FreeFootprintMapper;
import me.ajaja.module.footprint.mapper.KptFootprintMapper;

@Repository
@RequiredArgsConstructor
public class CreateFootprintAdaptor implements CreateFootprintPort {
	private final FootprintJpaRepository footprintJpaRepository;
	private final FreeFootprintMapper freeFootprintMapper;
	private final KptFootprintMapper kptFootprintMapper;

	@Override
	public Long create(Footprint footprint) {
		FootprintEntity footprintEntity = null;
		if (footprint instanceof FreeFootprint) {
			footprintEntity = freeFootprintMapper.toEntity((FreeFootprint)footprint);
		}
		if (footprint instanceof KptFootprint) {
			footprintEntity = kptFootprintMapper.toEntity((KptFootprint)footprint);
		}

		return footprintJpaRepository.save(footprintEntity)
			.getId();
	}
}
