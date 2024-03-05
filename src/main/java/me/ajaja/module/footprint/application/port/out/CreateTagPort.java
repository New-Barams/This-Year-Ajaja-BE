package me.ajaja.module.footprint.application.port.out;

import java.util.List;

public interface CreateTagPort {
	/**
	 * request create tag and tag-footprint to tag domain CreateTagUseCase
	 * @param footprintId target that have tags
	 * @param tags the tags
	 */
	void create(Long footprintId, List<String> tags);
}
