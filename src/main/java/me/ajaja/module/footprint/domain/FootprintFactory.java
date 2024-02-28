package me.ajaja.module.footprint.domain;

import org.springframework.stereotype.Component;

import me.ajaja.module.footprint.dto.FootprintRequest;

@Component
public final class FootprintFactory {
	public Footprint init(Long userId, FootprintRequest.Create param) {
		return switch (param.getType()) {
			case FREE -> new FreeFootprint(
				Target.init(param.getTargetId()),
				Writer.init(userId),
				param.getType(),
				Title.init(param.getTitle()),
				param.isVisible(),
				param.getContent()
			);
			case KPT -> new KptFootprint(
				Target.init(param.getTargetId()),
				Writer.init(userId),
				param.getType(),
				Title.init(param.getTitle()),
				param.isVisible(),
				param.getKeepContent(),
				param.getProblemContent(),
				param.getTryContent()
			);
		};
	}
}
