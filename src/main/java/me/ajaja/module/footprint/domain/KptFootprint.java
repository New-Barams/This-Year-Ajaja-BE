package me.ajaja.module.footprint.domain;

import java.sql.Blob;

import lombok.Getter;
import me.ajaja.module.footprint.dto.FootprintParam;

@Getter
public class KptFootprint extends Footprint {
	private Blob KEEP;
	private Blob PROBLEM;
	private Blob TRY;

	public KptFootprint(FootprintParam.Create footprintParam, Blob KEEP, Blob PROBLEM, Blob TRY) {
		super(footprintParam);
		this.KEEP = KEEP;
		this.PROBLEM = PROBLEM;
		this.TRY = TRY;
	}
}
