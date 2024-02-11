package me.ajaja.module.footprint.domain;

import java.sql.Blob;

import lombok.Getter;
import me.ajaja.module.footprint.dto.FootprintParam;

@Getter
public class FreeFootprint extends Footprint {
	private Blob CONTENT;

	public FreeFootprint(FootprintParam.Create footprintParam, Blob CONTENT) {
		super(footprintParam);
		this.CONTENT = CONTENT;
	}
}
