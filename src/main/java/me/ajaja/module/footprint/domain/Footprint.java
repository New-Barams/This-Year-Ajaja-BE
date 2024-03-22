package me.ajaja.module.footprint.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;
import me.ajaja.module.footprint.dto.FootprintRequest;

@Getter
@AllArgsConstructor
public abstract class Footprint extends SelfValidating<Footprint> {
	public enum Type {
		FREE, AJAJA
	}

	private final Long id;
	private final Target target;
	private final Writer writer;
	private final Type type;

	private Integer iconNumber;
	private Title title;
	private boolean visible;
	private boolean deleted;

	public Footprint(Target target, Writer writer, Type type, Integer iconNumber, Title title, boolean visible) {
		this(null, target, writer, type, iconNumber, title, visible, false);
	}

	public static Footprint init(Long userId, FootprintRequest.Create param) {
		return switch (param.getType()) {
			case FREE -> new FreeFootprint(
				Target.init(param.getTargetId()),
				Writer.init(userId),
				param.getType(),
				param.getIconNumber(),
				Title.init(param.getTitle()),
				param.isVisible(),
				new FreeContent(param.getContent())
			);
			case AJAJA -> new AjajaFootprint(
				Target.init(param.getTargetId()),
				Writer.init(userId),
				param.getType(),
				param.getIconNumber(),
				Title.init(param.getTitle()),
				param.isVisible(),
				new AjajaContent(
					param.getEmotion(),
					param.getReason(),
					param.getStrengths(),
					param.getWeaknesses(),
					param.getJujuljujul()
				)
			);
		};
	}
}
