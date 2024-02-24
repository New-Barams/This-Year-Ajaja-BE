package me.ajaja.module.footprint.application.port.out;

import me.ajaja.module.footprint.domain.Footprint;

public interface GetFootprintPort {
	Footprint getFootprint(Long id);

}
