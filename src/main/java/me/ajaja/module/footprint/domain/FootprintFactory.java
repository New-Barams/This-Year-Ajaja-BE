package me.ajaja.module.footprint.domain;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Component;

import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.footprint.dto.FootprintParam;

@Component
public final class FootprintFactory {
	public Footprint create(FootprintParam.Create param) {
		Footprint footprint = create(
			param.getTarget(),
			param.getWriter(),
			param.getType(),
			param.getTitle(),
			param.isVisible()
		);
		footprint.setItems(param);

		return footprint;
	}

	private Footprint create(Target target, Writer writer, Footprint.Type type, Title title, boolean visible) {
		switch (type) {
			case FREE:
				return new FreeFootprint(target, writer, type, title, visible);
			case KPT:
				return new KptFootprint(target, writer, type, title, visible);
			default:
				throw new AjajaException(INVALID_FOOTPRINT_TYPE);
		}
	}
}
