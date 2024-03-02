package me.ajaja.module.footprint.application.port;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.ajaja.module.footprint.application.port.in.CreateFootprintUseCase;
import me.ajaja.module.footprint.application.port.out.CreateFootprintPort;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.dto.FootprintRequest;
import me.ajaja.module.tag.application.port.out.CreateTagPort;

@Service
@Transactional
public class CreateFootprintService implements CreateFootprintUseCase {
	private final CreateFootprintPort createFootprintPort;
	private final CreateTagPort createTagPort;

	public CreateFootprintService(
		CreateFootprintPort createFootprintPort,
		@Qualifier("footprint") CreateTagPort createTagPort
	) {
		this.createFootprintPort = createFootprintPort;
		this.createTagPort = createTagPort;
	}

	@Override
	public void create(Long userId, FootprintRequest.Create param) {
		Footprint footprint = Footprint.init(userId, param);
		Long createdFootprintId = createFootprintPort.create(footprint);
		createTagPort.createTags(createdFootprintId, param.getTags());

	}
}
