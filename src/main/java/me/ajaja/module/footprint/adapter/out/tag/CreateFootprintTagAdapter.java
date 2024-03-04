package me.ajaja.module.footprint.adapter.out.tag;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.footprint.application.port.out.CreateTagPort;
import me.ajaja.module.tag.application.port.in.CreateTagUseCase;

@Component
@Transactional
@RequiredArgsConstructor
public class CreateFootprintTagAdapter implements CreateTagPort {
	private final CreateTagUseCase createTagUseCase;

	@Override
	public void create(Long footprintId, List<String> tags) {
		createTagUseCase.create(CreateTagUseCase.Type.FOOTPRINT, footprintId, tags);
	}
}
