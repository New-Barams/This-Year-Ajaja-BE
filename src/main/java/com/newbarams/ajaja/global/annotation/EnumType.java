package com.newbarams.ajaja.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.newbarams.ajaja.global.common.CustomEnumJsonDeserializer;

import jakarta.validation.Constraint;

/**
 * 클라이언트로부터 받아올 값이 Enum일 경우 사용하는 Annotation.
 * EnumInputValidator를 통해서 포함하는 Enum인지 검증하고 Json이 역직렬화될 때 CustomEnumDeserializer를 사용한다.
 * 반드시 @Schema에 allowableValues를 사용해야 한다.
 * @see CustomEnumJsonDeserializer
 * @see EnumTypeValidator
 * @author hejow
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumTypeValidator.class)
public @interface EnumType {
	String message() default "해당 필드에서 지원하지 않는 값입니다.";

	/**
	 * 어노테이션의 대상이되는 Enum
	 */
	Class<? extends Enum> enumClass();

	Class[] groups() default {};

	Class[] payload() default {};
}
