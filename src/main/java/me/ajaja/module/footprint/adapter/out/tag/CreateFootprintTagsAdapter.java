package me.ajaja.module.footprint.adapter.out.tag;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.footprint.application.port.out.CreateTagsPort;
import me.ajaja.module.tag.application.CreateFootprintTagService;

@Component
@Transactional
@RequiredArgsConstructor
public class CreateFootprintTagsAdapter implements CreateTagsPort {
	private final CreateFootprintTagService createTagService;

	@Override
	public void create(Long footprintId, List<String> tags) {
		createTagService.create(footprintId, tags);
	}
}
