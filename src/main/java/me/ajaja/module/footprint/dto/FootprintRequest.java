package me.ajaja.module.footprint.dto;

import java.util.List;

import lombok.Data;
import me.ajaja.module.footprint.domain.Footprint;

public final class FootprintRequest {
	@Data
	public static class Create {
		private final Long targetId;
		private final Footprint.Type type;
		private final String title;
		private final boolean visible;
		private final String content;
		private final String keepContent;
		private final String problemContent;
		private final String tryContent;
		private final List<String> tags;
	}
}
