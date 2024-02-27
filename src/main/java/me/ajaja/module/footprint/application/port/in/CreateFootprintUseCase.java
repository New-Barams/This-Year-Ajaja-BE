package me.ajaja.module.footprint.application.port.in;

import me.ajaja.module.footprint.dto.FootprintRequest;

public interface CreateFootprintUseCase {
	Long create(Long userId, FootprintRequest.Create param);
}
