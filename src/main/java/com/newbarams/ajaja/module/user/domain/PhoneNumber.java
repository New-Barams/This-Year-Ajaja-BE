package com.newbarams.ajaja.module.user.domain;

import java.beans.ConstructorProperties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.newbarams.ajaja.global.common.SelfValidating;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.exception.ErrorCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PhoneNumber extends SelfValidating<PhoneNumber> {
	private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d+");

	@NotBlank
	@Size(max = 11, min = 10) // min : 010-123-4567, max : 010-1234-5678
	private final String phoneNumber;

	@ConstructorProperties("phoneNumber")
	public PhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		this.validateSelf();
		validateNumeric(phoneNumber);
	}

	private void validateNumeric(String phoneNumber) {
		Matcher isNumeric = NUMERIC_PATTERN.matcher(phoneNumber);

		if (!isNumeric.matches()) {
			throw new AjajaException(ErrorCode.NON_NUMERIC_INPUT);
		}
	}
}
