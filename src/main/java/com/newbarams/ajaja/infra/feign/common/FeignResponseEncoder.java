package com.newbarams.ajaja.infra.feign.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FeignResponseEncoder {
	private static final String EMPTY_REQUEST = "EMPTY REQUEST! Request May Not Entered!";
	private static final String EMPTY_RESPONSE = "EMPTY RESPONSE!";

	String encodeRequestBody(Request request) {
		return request == null ? EMPTY_REQUEST : new String(request.body(), StandardCharsets.UTF_8);
	}

	String encodeResponseBody(Response.Body body) {
		return body == null ? EMPTY_RESPONSE : convertToString(body);
	}

	private String convertToString(Response.Body body) {
		try {
			return new String(body.asInputStream().readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			log.info("[Feign] Response Body Convert Failed");
			return EMPTY_RESPONSE;
		}
	}
}
