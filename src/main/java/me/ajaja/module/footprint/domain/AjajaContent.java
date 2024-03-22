package me.ajaja.module.footprint.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class AjajaContent extends SelfValidating<AjajaContent> {
	@NotBlank
	private final String emotion;
	@NotBlank
	private final String reason;
	@NotBlank
	private final String strengths;
	@NotBlank
	private final String weaknesses;
	@NotBlank
	private final String jujuljujul;

	public AjajaContent(String emotion, String reason, String strengths, String weaknesses, String jujuljujul) {
		this.emotion = emotion;
		this.reason = reason;
		this.strengths = strengths;
		this.weaknesses = weaknesses;
		this.jujuljujul = jujuljujul;
		validateSelf();
	}
}
