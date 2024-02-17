package me.ajaja.module.footprint.dto;

import java.beans.ConstructorProperties;

import lombok.Data;
import me.ajaja.module.footprint.domain.Target;
import me.ajaja.module.footprint.domain.Title;
import me.ajaja.module.footprint.domain.Writer;

public final class FootprintParam {
	@Data
	public static class Create {
		private final Target target;
		private final Writer writer;
		private final Title title;
		private final boolean visible;

		@ConstructorProperties({"target", "writer", "title", "visible"})
		public Create(Target target, Writer writer, Title title, boolean visible) {
			this.target = target;
			this.writer = writer;
			this.title = title;
			this.visible = visible;
		}
	}
}
