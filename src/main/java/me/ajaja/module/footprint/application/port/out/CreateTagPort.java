package me.ajaja.module.footprint.application.port.out;

import java.util.List;

public interface CreateTagPort {
	void create(Long footprintId, List<String> tags);
}
