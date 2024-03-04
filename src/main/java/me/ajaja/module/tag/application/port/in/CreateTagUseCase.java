package me.ajaja.module.tag.application.port.in;

import java.util.List;

public interface CreateTagUseCase {
	enum Type { PLAN, FOOTPRINT }

	void create(Type type, Long id, List<String> tags);
}
