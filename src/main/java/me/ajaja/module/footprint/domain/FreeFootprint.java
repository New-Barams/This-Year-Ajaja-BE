package me.ajaja.module.footprint.domain;

import lombok.Getter;
import me.ajaja.module.footprint.dto.FootprintParam;

@Getter
public class FreeFootprint extends Footprint {
	private String content;

	public FreeFootprint(FootprintParam.Create footprintParam, String content) {
		super(footprintParam);
		this.content = content;
	}
}
