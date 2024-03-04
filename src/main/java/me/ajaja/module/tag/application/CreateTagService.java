package me.ajaja.module.tag.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.tag.application.port.in.CreateTagUseCase;
import me.ajaja.module.tag.application.port.out.CreateTagPort;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTagService implements CreateTagUseCase {
	private final CreateTagPort createTagPort;

	@Override
	public void create(Type type, Long id, List<String> tags) {
		createTagPort.create(type, id, tags);
	}
}
