package me.ajaja.module.footprint.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class KptFootprint extends Footprint {
	@NotBlank
	private String keepContent;
	@NotBlank
	private String problemContent;
	@NotBlank
	private String tryContent;

	public KptFootprint(Long id, Target target, Writer writer, Type type, Title title, boolean visible, boolean deleted,
		String keepContent, String problemContent, String tryContent) {
		super(id, target, writer, type, title, visible, deleted);
		this.keepContent = keepContent;
		this.problemContent = problemContent;
		this.tryContent = tryContent;
		this.validateSelf();
	}

	public KptFootprint(Target target, Writer writer, Type type, Title title, boolean visible, String keepContent,
		String problemContent, String tryContent) {
		super(target, writer, type, title, visible);
		this.keepContent = keepContent;
		this.problemContent = problemContent;
		this.tryContent = tryContent;
		this.validateSelf();
	}
}
