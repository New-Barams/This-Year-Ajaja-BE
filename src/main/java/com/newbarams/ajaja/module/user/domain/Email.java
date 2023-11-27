package com.newbarams.ajaja.module.user.domain;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import com.newbarams.ajaja.global.common.SelfValidating;
import com.newbarams.ajaja.global.exception.AjajaException;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends SelfValidating<Email> {
	private static final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

	@NotBlank
	@Pattern(regexp = EMAIL_REGEXP)
	private String email;

	@NotBlank
	@Pattern(regexp = EMAIL_REGEXP)
	private String remindEmail;

	private boolean isVerified;

	private Email(String email, String remindEmail, boolean isVerified) {
		this.email = email;
		this.remindEmail = remindEmail;
		this.isVerified = isVerified;
		this.validateSelf();
	}

	public Email(String email) {
		this(email, email, false);
	}

	Email verified(String requestEmail) {
		return remindEmail.equals(requestEmail) ? new Email(email, remindEmail, true) :
			new Email(email, requestEmail, true);
	}

	void validateVerifiable(String requestEmail) {
		if (this.isVerified() && remindEmail.equals(requestEmail)) {
			throw new AjajaException(UNABLE_TO_VERIFY_EMAIL);
		}
	}
}
