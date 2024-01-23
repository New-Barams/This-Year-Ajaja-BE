package com.newbarams.ajaja.module.user.domain;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import java.beans.ConstructorProperties;

import com.newbarams.ajaja.global.common.SelfValidating;
import com.newbarams.ajaja.global.exception.AjajaException;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class Email extends SelfValidating<Email> {
	private static final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

	@Pattern(regexp = EMAIL_REGEXP)
	private final String signUpEmail;

	@Pattern(regexp = EMAIL_REGEXP)
	private final String remindEmail;

	private final boolean verified;

	@ConstructorProperties({"signUpEmail", "remindEmail", "verified"})
	public Email(String signUpEmail, String remindEmail, boolean verified) {
		this.signUpEmail = signUpEmail;
		this.remindEmail = remindEmail;
		this.verified = verified;
		this.validateSelf();
	}

	public static Email init(String email) {
		return new Email(email, email, false);
	}

	void validateVerifiable(String email) {
		if (this.verified && isSameRemindEmail(email)) {
			throw new AjajaException(ALREADY_VERIFY_EMAIL);
		}
	}

	Email verified(String email) {
		return isSameRemindEmail(email) ? Email.verify(signUpEmail, remindEmail) : Email.verify(signUpEmail, email);
	}

	private static Email verify(String signUpEmail, String remindEmail) {
		return new Email(signUpEmail, remindEmail, true);
	}

	private boolean isSameRemindEmail(String email) {
		return remindEmail.equals(email);
	}
}
