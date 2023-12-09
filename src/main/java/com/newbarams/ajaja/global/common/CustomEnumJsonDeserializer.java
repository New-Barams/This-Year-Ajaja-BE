package com.newbarams.ajaja.global.common;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomEnumJsonDeserializer<T extends Enum<T>> extends JsonDeserializer<T> {

	@Override
	public T deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
		final String target = parser.getValueAsString();
		final Class<?> dtoClass = parser.getCurrentValue().getClass();
		final String field = parser.currentName();

		try {
			final Class<T> enumClass = extractEnumClass(dtoClass, field);
			return Enum.valueOf(enumClass, target.toUpperCase()); // client로 받아오는 enum은 lower case
		} catch (IllegalArgumentException | NoSuchFieldException e) {
			return null;
		}
	}

	private static <T extends Enum<T>> Class<T> extractEnumClass(
		Class<?> dtoClass,
		String fieldName
	) throws NoSuchFieldException {
		Field field = dtoClass.getDeclaredField(fieldName);
		return (Class<T>)field.getType();
	}
}
