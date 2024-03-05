package me.ajaja.module.tag.application.port.out;

import java.util.List;

import me.ajaja.module.tag.application.port.in.CreateTagsUseCase;

public interface CreateTagsPort {
	void create(CreateTagsUseCase.Type type, Long targetId, List<String> tagNames);
}
