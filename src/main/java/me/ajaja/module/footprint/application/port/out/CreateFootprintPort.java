package me.ajaja.module.footprint.application.port.out;

import me.ajaja.module.footprint.domain.Footprint;

public interface CreateFootprintPort {
	Long create(Footprint footprint);
}
