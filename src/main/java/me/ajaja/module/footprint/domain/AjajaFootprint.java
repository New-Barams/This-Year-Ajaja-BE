package me.ajaja.module.footprint.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AjajaFootprint extends Footprint {
	@NotBlank
	private String emotion;
	@NotBlank
	private String reason;
	@NotBlank
	private String strengths;
	@NotBlank
	private String weaknesses;
	@NotBlank
	private String jujuljujul;

	public AjajaFootprint(Long id, Target target, Writer writer, Type type, Integer iconNumber, Title title,
		boolean visible, boolean deleted, String emotion, String reason, String strengths, String weaknesses,
		String jujuljujul
	) {
		super(id, target, writer, type, iconNumber, title, visible, deleted);
		this.emotion = emotion;
		this.reason = reason;
		this.strengths = strengths;
		this.weaknesses = weaknesses;
		this.jujuljujul = jujuljujul;
		this.validateSelf();
	}

	public AjajaFootprint(Target target, Writer writer, Type type, Integer iconNumber, Title title, boolean visible,
		String emotion, String reason, String strengths, String weaknesses, String jujuljujul
	) {
		super(target, writer, type, iconNumber, title, visible);
		this.emotion = emotion;
		this.reason = reason;
		this.strengths = strengths;
		this.weaknesses = weaknesses;
		this.jujuljujul = jujuljujul;
		this.validateSelf();
	}
}
