package me.ajaja.module.tag.application.port.out;

import java.util.List;

public interface CreateTagPort {
	List<String> create(Long targetId, List<String> tagNames);
}
