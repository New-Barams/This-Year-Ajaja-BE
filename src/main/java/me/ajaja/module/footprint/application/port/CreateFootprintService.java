package me.ajaja.module.footprint.application.port;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.footprint.application.port.in.CreateFootprintUseCase;
import me.ajaja.module.footprint.application.port.out.CreateFootprintPort;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.dto.FootprintRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateFootprintService implements CreateFootprintUseCase {
	private final CreateFootprintPort createFootprintPort;

	@Override
	public void create(Long userId, FootprintRequest.Create param) {
		Footprint footprint = Footprint.init(userId, param);
		createFootprintPort.create(footprint);
	}
}
