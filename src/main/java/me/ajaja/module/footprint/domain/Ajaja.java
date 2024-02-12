package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Ajaja extends SelfValidating<Ajaja> {
	@NotNull
	private final Long userId;

	@NotBlank
	@Size(max = 20)
	private final String nickName;

	@ConstructorProperties({"userId", "nickName"})
	public Ajaja(Long userId, String nickName) {
		this.userId = userId;
		this.nickName = nickName;
		this.validateSelf();
	}
}
