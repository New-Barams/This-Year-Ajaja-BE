package me.ajaja.module.footprint.dto;

import lombok.Data;

public final class FootprintRequest {
	@Data
	public static class Create {
		private final FootprintParam.Create param;
	}
}
