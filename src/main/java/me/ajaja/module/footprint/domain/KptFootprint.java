package me.ajaja.module.footprint.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import me.ajaja.module.footprint.dto.FootprintParam;

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

	public KptFootprint(Target target, Writer writer, Type type, Title title, boolean visible) {
		super(target, writer, type, title, visible);
	}

	@Override
	public void setItems(FootprintParam.Create param) {
		this.keepContent = param.getKeepContent();
		this.problemContent = param.getProblemContent();
		this.tryContent = param.getTryContent();
		this.validateSelf();
	}
}
