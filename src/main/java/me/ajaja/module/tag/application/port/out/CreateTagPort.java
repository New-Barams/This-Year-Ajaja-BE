package me.ajaja.module.tag.application.port.out;

import java.util.List;

import me.ajaja.module.tag.application.port.in.CreateTagUseCase;

public interface CreateTagPort {
	void create(CreateTagUseCase.Type type, Long targetId, List<String> tagNames);
}
