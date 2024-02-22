package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Writer extends SelfValidating<Writer> {
	@NotNull
	private final Long id;

	@NotBlank
	@Size(max = 20)
	private final String nickname;

	@ConstructorProperties({"id", "nickname"})
	public Writer(Long id, String nickname) {
		this.id = id;
		this.nickname = nickname;
		this.validateSelf();
	}
}
