package me.ajaja.module.footprint.application.port.in;

import java.util.List;

import me.ajaja.module.footprint.dto.FootprintParam;

public interface CreateFootprintUseCase {
	Long create(Long userId, Long targetId, FootprintParam.Create param, List<String> tags);
}
