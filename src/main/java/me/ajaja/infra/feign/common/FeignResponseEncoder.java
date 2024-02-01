package me.ajaja.infra.feign.common;

import static java.nio.charset.StandardCharsets.*;

import java.io.IOException;

import org.springframework.stereotype.Component;

import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FeignResponseEncoder {
	private static final String EMPTY_REQUEST = "Request is EMPTY. Request may not entered.";
	private static final String EMPTY_RESPONSE = "Response is EMPTY";

	String encodeRequestBody(Request request) {
		return request == null ? EMPTY_REQUEST : new String(request.body(), UTF_8);
	}

	String encodeResponseBody(Response.Body body) {
		return body == null ? EMPTY_RESPONSE : convertToString(body);
	}

	private String convertToString(Response.Body body) {
		try {
			return new String(body.asInputStream().readAllBytes(), UTF_8);
		} catch (IOException e) {
			log.info("[Feign] Response Body Convert Failed");
			return EMPTY_RESPONSE;
		}
	}
}
