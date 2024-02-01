package me.ajaja.module.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserId {
	private final Long id;
	private final Long oauthId;

	public static UserId from(Long oauthId) {
		return new UserId(null, oauthId);
	}
}
