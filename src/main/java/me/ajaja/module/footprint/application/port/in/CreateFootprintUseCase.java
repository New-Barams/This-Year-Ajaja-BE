package me.ajaja.module.footprint.application.port.in;

import me.ajaja.module.footprint.dto.FootprintRequest;

public interface CreateFootprintUseCase {
	void create(Long userId, FootprintRequest.Create param);
}
