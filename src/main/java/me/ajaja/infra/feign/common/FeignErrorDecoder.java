package me.ajaja.infra.feign.common;

import static me.ajaja.global.exception.ErrorCode.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.exception.AjajaException;

@Slf4j
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {
	private static final int SERVER_UNAVAILABLE = 503;
	private static final int TOO_MANY_REQUEST = 429;

	private final FeignResponseEncoder feignResponseEncoder;

	@Override
	public Exception decode(String methodKey, Response response) {
		return switch (response.status()) {
			case TOO_MANY_REQUEST, SERVER_UNAVAILABLE -> retry(response);
			default -> decodeFeignException(response);
		};
	}

	/**
	 * todo: NPE occurs depends on platform
	 * naver : {"error": {"errorCode": "...", "message": "..."}}
	 * kauth : {"error": "...", "error_description": "..."}
	 * kapi : {"msg": "...", "code": "..."}
	 */
	private Exception decodeFeignException(Response response) {
		String requestBody = feignResponseEncoder.encodeRequestBody(response.request());
		String responseBody = feignResponseEncoder.encodeResponseBody(response.body());

		log.info("[Feign] API Request Fail To {} By '{}'.", response.request().url(), response.reason());
		logHeaders(response.request().headers());

		log.info("[Feign] Failed Request Body : {}", requestBody);
		log.info("[Feign] API Response : {}", responseBody);

		return new AjajaException(EXTERNAL_API_FAIL);
	}

	private RuntimeException retry(Response response) {
		log.info("[Feign] Retrying API Request To {}", response.request().url());

		return new RetryableException(
			response.status(),
			response.reason(),
			response.request().httpMethod(),
			new Date(System.currentTimeMillis()),
			response.request()
		);
	}

	private void logHeaders(Map<String, Collection<String>> headers) {
		headers.forEach((key, value) -> log.info("[Feign] Failed Header -> {} : {}", key, value.toString()));
	}
}
