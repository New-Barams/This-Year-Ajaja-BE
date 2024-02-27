package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Writer extends SelfValidating<Writer> {
	@NotNull
	private final Long id;
	private final String nickname;

	@ConstructorProperties({"id", "nickname"})
	public Writer(Long id, String nickname) {
		this.id = id;
		this.nickname = nickname;
		this.validateSelf();
	}

	public Writer(Long id) {
		this(id, null);
	}
}
