package com.newbarams.ajaja.infra.feign.common;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {
	@Override
	public Exception decode(String methodKey, Response response) {
		// todo: logic
		return null;
	}
}
