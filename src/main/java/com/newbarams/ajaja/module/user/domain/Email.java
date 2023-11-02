package com.newbarams.ajaja.module.user.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends SelfValidating<Email> {
	private static final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

	@NotBlank
	@Pattern(regexp = EMAIL_REGEXP)
	private String email;

	private boolean isVerified;

	public Email(String email) {
		this.email = email;
		this.isVerified = false;
		this.validateSelf();
	}

	void verified() {
		this.isVerified = true;
	}

	boolean isVerified() {
		return isVerified;
	}
}
