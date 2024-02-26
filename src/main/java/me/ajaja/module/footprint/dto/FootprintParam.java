package me.ajaja.module.footprint.dto;

import java.beans.ConstructorProperties;

import lombok.Data;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.Title;

public final class FootprintParam {
	@Data
	public static class Create {
		private final Footprint.Type type;
		private final Title title;
		private final boolean visible;
		private final String content;
		private final String keepContent;
		private final String problemContent;
		private final String tryContent;

		@ConstructorProperties({"type", "title", "visible", "content", "keepContent",
			"problemContent", "tryContent"})
		public Create(Footprint.Type type, Title title, boolean visible, String content,
			String keepContent, String problemContent, String tryContent) {
			this.type = type;
			this.title = title;
			this.visible = visible;
			this.content = content;
			this.keepContent = keepContent;
			this.problemContent = problemContent;
			this.tryContent = tryContent;
		}
	}
}
