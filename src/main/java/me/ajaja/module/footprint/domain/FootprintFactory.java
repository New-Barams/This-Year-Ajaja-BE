package me.ajaja.module.footprint.domain;

import org.springframework.stereotype.Component;

import me.ajaja.module.footprint.dto.FootprintParam;

@Component
public final class FootprintFactory {
	public Footprint create(FootprintParam.Create param) {
		return switch (param.getType()) {
			case FREE -> new FreeFootprint(
				param.getTarget(),
				param.getWriter(),
				param.getType(),
				param.getTitle(),
				param.isVisible(),
				param.getContent()
			);
			case KPT -> new KptFootprint(
				param.getTarget(),
				param.getWriter(),
				param.getType(),
				param.getTitle(),
				param.isVisible(),
				param.getKeepContent(),
				param.getProblemContent(),
				param.getTryContent()
			);
		};
	}
}
