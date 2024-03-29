package me.ajaja.module.footprint.dto;

import java.util.List;

import lombok.Data;
import me.ajaja.module.footprint.domain.Footprint;

public final class FootprintRequest {
	@Data
	public static class Create {
		private final Long targetId;
		private final Footprint.Type type;
		private final Integer iconNumber;
		private final String title;
		private final boolean visible;
		private final String content;
		private final String emotion;
		private final String reason;
		private final String strengths;
		private final String weaknesses;
		private final String jujuljujul;
		private final List<String> tags;
	}
}
