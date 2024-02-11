package me.ajaja.module.footprint.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import me.ajaja.module.footprint.dto.FootprintParam;

@Getter
public abstract class Footprint {
	public enum Type {
		FREE, KPT
	}

	public enum Visibility {
		PUBLIC, PRIVATE
	}

	private final TargetPlan targetPlan;
	private final Writer writer;

	private Title title;
	private Type type;
	private Visibility visibility;

	private Set<Tag> tags;
	private List<Ajaja> ajajas;

	public Footprint(FootprintParam.Create footprintParam) {
		this.targetPlan = footprintParam.getTargetPlan();
		this.writer = footprintParam.getWriter();
		this.title = footprintParam.getTitle();
		this.type = footprintParam.getType();
		this.visibility = footprintParam.getVisibility();
		this.tags = (tags == null) ? new HashSet<>() : footprintParam.getTags();
		this.ajajas = (ajajas == null) ? new ArrayList<>() : footprintParam.getAjajas();
	}
}
