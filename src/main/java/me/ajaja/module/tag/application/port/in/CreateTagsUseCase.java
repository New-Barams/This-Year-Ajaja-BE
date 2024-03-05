package me.ajaja.module.tag.application.port.in;

import java.util.List;

public interface CreateTagsUseCase {
	enum Type { PLAN, FOOTPRINT }

	/**
	 * create tag and tag-footprint from other domain request
	 * @param type domain that request
	 * @param id target that have tags
	 * @param tags tags
	 */
	void create(Type type, Long id, List<String> tags);
}
