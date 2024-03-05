package me.ajaja.module.footprint.adapter.out.tag;

import static me.ajaja.module.tag.application.port.in.CreateTagsUseCase.Type.*;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.footprint.application.port.out.CreateTagsPort;
import me.ajaja.module.tag.application.port.in.CreateTagsUseCase;

@Component
@Transactional
@RequiredArgsConstructor
public class CreateFootprintTagsAdapter implements CreateTagsPort {
	private final CreateTagsUseCase createTagUseCase;

	@Override
	public void create(Long footprintId, List<String> tags) {
		createTagUseCase.create(FOOTPRINT, footprintId, tags);
	}
}
