package me.ajaja.module.footprint.domain;

import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FreeFootprint extends Footprint {
	@NotBlank
	private String content;

	FreeFootprint(Target target, Writer writer, Title title, boolean visible, boolean deleted,
		Set<Tag> tags, List<Ajaja> ajajas, String content) {
		super(target, writer, title, visible, deleted, tags, ajajas);
		this.content = content;
		this.validateSelf();
	}
}
