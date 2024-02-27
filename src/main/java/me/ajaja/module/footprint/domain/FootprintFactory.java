package me.ajaja.module.footprint.domain;

import org.springframework.stereotype.Component;

import me.ajaja.module.footprint.dto.FootprintRequest;

@Component
public final class FootprintFactory {
	public Footprint create(Long userId, FootprintRequest.Create param) {
		return switch (param.getType()) {
			case FREE -> new FreeFootprint(
				new Target(param.getTargetId()),
				new Writer(userId),
				param.getType(),
				new Title(param.getTitle()),
				param.isVisible(),
				param.getContent()
			);
			case KPT -> new KptFootprint(
				new Target(param.getTargetId()),
				new Writer(userId),
				param.getType(),
				new Title(param.getTitle()),
				param.isVisible(),
				param.getKeepContent(),
				param.getProblemContent(),
				param.getTryContent()
			);
		};
	}
}
