package me.ajaja.module.footprint.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import me.ajaja.module.footprint.dto.FootprintParam;

@Getter
public class FreeFootprint extends Footprint {
	@NotBlank
	private String content;

	public FreeFootprint(Long id, Target target, Writer writer, Type type, Title title, boolean visible,
		boolean deleted, String content) {
		super(id, target, writer, type, title, visible, deleted);
		this.content = content;
		this.validateSelf();
	}

	public FreeFootprint(Target target, Writer writer, Type type, Title title, boolean visible) {
		super(target, writer, type, title, visible);
	}

	@Override
	public void setItems(FootprintParam.Create param) {
		this.content = param.getContent();
		this.validateSelf();
	}
}
