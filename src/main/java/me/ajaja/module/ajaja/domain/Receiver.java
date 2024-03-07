package me.ajaja.module.ajaja.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Receiver extends SelfValidating<Receiver> {
	@NotNull
	private final Long userId;
	private final String email;
	private final String phoneNumber;

	public Receiver(Long userId, String email, String phoneNumber) {
		this.userId = userId;
		this.email = email;
		this.phoneNumber = phoneNumber;
		validateSelf();
	}
}
