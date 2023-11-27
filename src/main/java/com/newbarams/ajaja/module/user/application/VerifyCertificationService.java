package com.newbarams.ajaja.module.user.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.user.application.model.EmailVerification;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class VerifyCertificationService {
	private static final String KEY_PREFIX = "AJAJA "; // todo: manage prefix on same point

	private final RetrieveUserService retrieveUserService;
	private final RedisTemplate<String, Object> redisTemplate;

	public void verify(Long userId, String certification) {
		User user = retrieveUserService.loadExistUserById(userId);
		EmailVerification verification = getEmailVerificationFromCache(userId);
		verifyCertification(verification.certification(), certification);
		user.verified(verification.email());
	}

	private EmailVerification getEmailVerificationFromCache(Long userId) {
		if (redisTemplate.opsForValue().get(KEY_PREFIX + userId) instanceof EmailVerification verification) {
			return verification;
		}

		throw new AjajaException(CERTIFICATION_NOT_FOUND);
	}

	private void verifyCertification(String saved, String input) {
		if (!Objects.equals(saved, input)) {
			throw new AjajaException(CERTIFICATION_NOT_MATCH);
		}
	}
}
