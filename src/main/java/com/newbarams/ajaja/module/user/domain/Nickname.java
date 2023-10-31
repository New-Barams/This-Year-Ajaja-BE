package com.newbarams.ajaja.module.user.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nickname extends SelfValidating<Nickname> {
	@NotBlank
	@Size(max = 20)
	private String nickname;

	private Nickname(String nickname) {
		this.nickname = nickname;
		this.validateSelf();
	}

	public static Nickname generateNickname() {
		return new Nickname("default");
	}
}
