package me.ajaja.module.user.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

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

	static Nickname init() {
		return new Nickname(RandomNicknameGenerator.generate());
	}

	public static Nickname refresh() {
		return new Nickname(RandomNicknameGenerator.generate());
	}
}
