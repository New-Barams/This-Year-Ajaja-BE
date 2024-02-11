package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Ajaja extends SelfValidating<Ajaja> {
	@NotNull
	private final Long userId;

	@NotBlank
	private final String nickName;

	@ConstructorProperties({"userId", "userName"})
	public Ajaja(Long userId, String nickName) {
		this.userId = userId;
		this.nickName = nickName;
	}
}
