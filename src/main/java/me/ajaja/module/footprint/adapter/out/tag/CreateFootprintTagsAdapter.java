package me.ajaja.module.footprint.adapter.out.tag;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.footprint.application.port.out.CreateTagsPort;
import me.ajaja.module.tag.application.CreateFootprintTagsService;

@Component
@Transactional
@RequiredArgsConstructor
public class CreateFootprintTagsAdapter implements CreateTagsPort {
	private final CreateFootprintTagsService createTagsService;

	@Override
	public void create(Long footprintId, List<String> tags) {
		createTagsService.create(footprintId, tags);
	}
}
