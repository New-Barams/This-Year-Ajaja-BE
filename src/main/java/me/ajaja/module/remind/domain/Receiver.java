package me.ajaja.module.remind.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Receiver extends SelfValidating<Receiver> {
	public enum RemindType {
		KAKAO,
		EMAIL
	}

	@NotNull
	private final Long id;
	private final String email;
	private final String phoneNumber;
	private final RemindType type;

	public Receiver(Long id, String type, String email, String phoneNumber) {
		this.id = id;
		this.type = RemindType.valueOf(type);
		this.email = email;
		this.phoneNumber = phoneNumber;
		validateSelf();
	}
}
