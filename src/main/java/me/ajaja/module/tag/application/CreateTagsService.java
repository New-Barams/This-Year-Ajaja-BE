package me.ajaja.module.tag.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.tag.application.port.in.CreateTagsUseCase;
import me.ajaja.module.tag.application.port.out.CreateTagsPort;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTagsService implements CreateTagsUseCase {
	private final CreateTagsPort createTagPort;

	@Override
	public void create(Type type, Long id, List<String> tags) {
		createTagPort.create(type, id, tags);
	}
}
