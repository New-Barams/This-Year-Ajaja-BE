package com.newbarams.ajaja.common.util;

import java.io.UnsupportedEncodingException;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Raw parser to handle checked exception (I/O, Encoding)
 * @author hejow
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class JsonParser {
	private static final ObjectMapper mapper = new ObjectMapper();

	@FunctionalInterface
	interface ResultContentSupplier {
		String get() throws UnsupportedEncodingException;
	}

	@Nullable
	public static JsonNode readTree(ResultContentSupplier contentSupplier) {
		try {
			String content = contentSupplier.get();
			return content != null ? mapper.readTree(content) : null;
		} catch (UnsupportedEncodingException | JsonProcessingException exception) {
			throw new IllegalArgumentException(exception);
		}
	}
}
