package me.ajaja.module.footprint.dto;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Set;

import lombok.Data;
import me.ajaja.module.footprint.domain.Ajaja;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FootprintStatus;
import me.ajaja.module.footprint.domain.Tag;
import me.ajaja.module.footprint.domain.Target;
import me.ajaja.module.footprint.domain.Title;
import me.ajaja.module.footprint.domain.Writer;

public final class FootprintParam {
	@Data
	public static class Create {
		private final Target targetPlan;
		private final Writer writer;

		private final Title title;
		private final Footprint.Type type;
		private final FootprintStatus footprintStatus;

		private final Set<Tag> tags;
		private final List<Ajaja> ajajas;

		@ConstructorProperties({"targetPlan", "writer", "title", "type", "footprintStatus", "tags", "ajajas"})
		public Create(Target targetPlan, Writer writer, Title title, Footprint.Type type,
			FootprintStatus footprintStatus, Set<Tag> tags, List<Ajaja> ajajas
		) {
			this.targetPlan = targetPlan;
			this.writer = writer;
			this.title = title;
			this.type = type;
			this.footprintStatus = footprintStatus;
			this.tags = tags;
			this.ajajas = ajajas;
		}
	}
}
