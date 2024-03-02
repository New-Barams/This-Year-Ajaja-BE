package me.ajaja.module.tag.application.port.out;

import java.util.List;

public interface CreateTagPort {
	void createTags(Long targetId, List<String> tagNames);
}
