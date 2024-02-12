package me.ajaja.module.footprint.domain;

import lombok.Getter;
import me.ajaja.module.footprint.dto.FootprintParam;

@Getter
public class KptFootprint extends Footprint {
	private String keepContent;
	private String problem;
	private String tryContent;

	public KptFootprint(FootprintParam.Create footprintParam, String keepContent, String problem, String tryContent) {
		super(footprintParam);
		this.keepContent = keepContent;
		this.problem = problem;
		this.tryContent = tryContent;
	}
}
