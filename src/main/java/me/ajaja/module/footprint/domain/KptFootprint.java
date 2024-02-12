package me.ajaja.module.footprint.domain;

import java.sql.Blob;

import lombok.Getter;
import me.ajaja.module.footprint.dto.FootprintParam;

@Getter
public class KptFootprint extends Footprint {
	private Blob keepContent;
	private Blob problem;
	private Blob tryContent;

	public KptFootprint(FootprintParam.Create footprintParam, Blob keepContent, Blob problem, Blob tryContent) {
		super(footprintParam);
		this.keepContent = keepContent;
		this.problem = problem;
		this.tryContent = tryContent;
	}
}
