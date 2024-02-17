package me.ajaja.module.footprint.domain;

import me.ajaja.module.footprint.dto.FootprintParam;

public class FootprintFactory {
	public static FreeFootprint createFreeFootprint(FootprintParam.Create create, String content) {
		return new FreeFootprint(
			null,
			create.getTarget(),
			create.getWriter(),
			create.getTitle(),
			create.isVisible(),
			false,
			create.getTags(),
			content
		);
	}

	public static KptFootprint createKptFootprint(
		FootprintParam.Create create, String keepContent, String problemContent, String tryContent
	) {
		return new KptFootprint(
			null,
			create.getTarget(),
			create.getWriter(),
			create.getTitle(),
			create.isVisible(),
			false,
			create.getTags(),
			keepContent,
			problemContent,
			tryContent
		);
	}
}
