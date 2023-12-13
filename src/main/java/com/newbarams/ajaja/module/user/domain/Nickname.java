package com.newbarams.ajaja.module.user.domain;

import java.beans.ConstructorProperties;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class Nickname extends SelfValidating<Nickname> {
	@NotBlank
	@Size(max = 20)
	private final String nickname;

	@ConstructorProperties("nickname")
	public Nickname(String nickname) {
		this.nickname = nickname;
		this.validateSelf();
	}

	public static Nickname renew() {
		return new Nickname(RandomNicknameGenerator.generate());
	}
}
