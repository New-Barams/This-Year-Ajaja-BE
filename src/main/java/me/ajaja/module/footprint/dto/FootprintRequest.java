package me.ajaja.module.footprint.dto;

import java.beans.ConstructorProperties;
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

		@ConstructorProperties({"targetId", "type", "title", "visible", "content", "keepContent", "problemContent",
			"tryContent", "tags"})
		public Create(Long targetId, Footprint.Type type, String title, boolean visible, String content,
			String keepContent, String problemContent, String tryContent, List<String> tags) {
			this.targetId = targetId;
			this.type = type;
			this.title = title;
			this.visible = visible;
			this.content = content;
			this.keepContent = keepContent;
			this.problemContent = problemContent;
			this.tryContent = tryContent;
			this.tags = tags;
		}
	}
}
