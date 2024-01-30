package com.newbarams.ajaja.global.common;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public abstract class SelfValidating<T> {
	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@SuppressWarnings("unchecked")
	protected void validateSelf() {
		Set<ConstraintViolation<T>> violations = validator.validate((T)this);

		if (isValidationFailed(violations)) {
			throw new ConstraintViolationException(violations);
		}
	}

	private boolean isValidationFailed(Set<ConstraintViolation<T>> violations) {
		return !violations.isEmpty();
	}
}
