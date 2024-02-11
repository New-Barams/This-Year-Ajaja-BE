package me.ajaja.module.footprint.dto;

import java.util.List;
import java.util.Set;

import lombok.Data;
import me.ajaja.module.footprint.domain.Ajaja;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FootprintStatus;
import me.ajaja.module.footprint.domain.Tag;
import me.ajaja.module.footprint.domain.TargetPlan;
import me.ajaja.module.footprint.domain.Title;
import me.ajaja.module.footprint.domain.Writer;

public final class FootprintParam {
	@Data
	public static class Create {
		private final TargetPlan targetPlan;
		private final Writer writer;

		private Title title;
		private Footprint.Type type;
		private FootprintStatus footprintStatus;

		private Set<Tag> tags;
		private List<Ajaja> ajajas;
	}
}
