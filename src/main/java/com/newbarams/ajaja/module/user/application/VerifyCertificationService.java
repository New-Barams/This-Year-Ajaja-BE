package com.newbarams.ajaja.module.user.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.user.application.model.Verification;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class VerifyCertificationService {
	private final RetrieveUserService retrieveUserService;
	private final CacheUtil cacheUtil;

	public void verify(Long userId, String certification) {
		User user = retrieveUserService.loadExistUserById(userId);
		Verification verification = cacheUtil.getEmailVerification(userId);
		verifyCertification(verification.getCertification(), certification);
		user.verified(verification.getTarget());
	}

	private void verifyCertification(String saved, String input) {
		if (!Objects.equals(saved, input)) {
			throw new AjajaException(CERTIFICATION_NOT_MATCH);
		}
	}
}
