package com.newbarams.ajaja.infra.feign.ncp.client;

import static java.nio.charset.StandardCharsets.*;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.common.TimeValue;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class NaverFeignHeaderConfiguration {
	private static final String EPOCH_TIME_HEADER = "x-ncp-apigw-timestamp";
	private static final String ACCESS_KEY_HEADER = "x-ncp-iam-access-key";
	private static final String SIGNATURE_HEADER = "x-ncp-apigw-signature-v2";
	private static final String SIGNATURE_ALGORITHM = "HmacSHA256";

	private final NaverCloudProperties properties;

	@Bean
	RequestInterceptor naverCloudHeaderInterceptor() {
		return request -> {
			String timestamp = epochTime();

			request.header(EPOCH_TIME_HEADER, timestamp)
				.header(ACCESS_KEY_HEADER, properties.getAccessKey())
				.header(SIGNATURE_HEADER, generateSignature(request, timestamp));
		};
	}

	private String epochTime() {
		return String.valueOf(TimeValue.now().getTimeMillis());
	}

	private String generateSignature(RequestTemplate request, String timestamp) {
		String message = createMessage(request, timestamp);
		byte[] encryptedMessage = encryptFrom(message);
		return Base64.encodeBase64String(encryptedMessage);
	}

	private String createMessage(RequestTemplate request, String timestamp) {
		return String.format("%s %s\n%s\n%s", request.method(), request.url(), timestamp, properties.getAccessKey());
	}

	private byte[] encryptFrom(String message) {
		String secretKey = properties.getSecretKey();
		Key key = new SecretKeySpec(secretKey.getBytes(UTF_8), SIGNATURE_ALGORITHM);
		return encryptWithMac(key, message);
	}

	private byte[] encryptWithMac(Key key, String message) {
		try {
			Mac mac = Mac.getInstance(SIGNATURE_ALGORITHM);
			mac.init(key);
			return mac.doFinal(message.getBytes(UTF_8));
		} catch (NoSuchAlgorithmException | InvalidKeyException exception) {
			throw new IllegalArgumentException("Encrypt Failed By " + message, exception);
		}
	}
}
