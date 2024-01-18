package com.newbarams.ajaja.module.remind.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserInfo extends SelfValidating<UserInfo> {
	@NotNull
	private final Long id;
	private final String email;

	public UserInfo(Long id, String email) {
		this.id = id;
		this.email = email;
		validateSelf();
	}
}
