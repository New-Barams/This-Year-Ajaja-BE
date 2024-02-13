package me.ajaja.module.footprint.dto;

import java.beans.ConstructorProperties;
import java.util.Set;

import lombok.Data;
import me.ajaja.module.footprint.domain.Tag;
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
		private final Set<Tag> tags;

		@ConstructorProperties({"target", "writer", "title", "visible", "tags"})
		public Create(Target target, Writer writer, Title title, boolean visible, Set<Tag> tags) {
			this.target = target;
			this.writer = writer;
			this.title = title;
			this.visible = visible;
			this.tags = tags;
		}
	}
}
