package me.ajaja.module.remind.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Receiver extends SelfValidating<Receiver> {
	@NotNull
	private final Long id;
	private final String email;
	private final String phoneNumber;
	private final String remindType;

	public Receiver(Long id, String remindType, String email, String phoneNumber) {
		this.id = id;
		this.remindType = remindType;
		this.email = email;
		this.phoneNumber = phoneNumber;
		validateSelf();
	}
}
