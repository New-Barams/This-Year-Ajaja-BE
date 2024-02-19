package me.ajaja.module.footprint.dto;

import java.beans.ConstructorProperties;

import lombok.Data;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.Target;
import me.ajaja.module.footprint.domain.Title;
import me.ajaja.module.footprint.domain.Writer;

public final class FootprintParam {
	@Data
	public static class Create {
		private final Target target;
		private final Writer writer;
		private final Footprint.Type type;
		private final Title title;
		private final boolean visible;
		private final String content;
		private final String keepContent;
		private final String problemContent;
		private final String tryContent;

		@ConstructorProperties({"target", "writer", "type", "title", "visible", "content", "keepContent",
			"problemContent", "tryContent"})
		public Create(Target target, Writer writer, Footprint.Type type, Title title, boolean visible, String content,
			String keepContent, String problemContent, String tryContent) {
			this.target = target;
			this.writer = writer;
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
