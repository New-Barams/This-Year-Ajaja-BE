package com.newbarams.ajaja.global.annotation;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumTypeValidator implements ConstraintValidator<EnumType, Enum> {
	private List<? extends Enum> enumValues;

	@Override
	public void initialize(EnumType constraintAnnotation) {
		Class<? extends Enum> enumClass = constraintAnnotation.enumClass();
		enumValues = Arrays.stream(enumClass.getEnumConstants()).toList();
	}

	@Override
	public boolean isValid(Enum value, ConstraintValidatorContext context) {
		return value != null && enumValues.contains(value);
	}
}
