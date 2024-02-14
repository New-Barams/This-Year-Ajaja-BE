package me.ajaja.module.footprint.application.port.out;

import me.ajaja.module.footprint.domain.FreeFootprint;
import me.ajaja.module.footprint.domain.KptFootprint;

public interface GetFootprintPort {
	FreeFootprint getFreeFootprint(Long id);

	KptFootprint getKptFootprint(Long id);
}
