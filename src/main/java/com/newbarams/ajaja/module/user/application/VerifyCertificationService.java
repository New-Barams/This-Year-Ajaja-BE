package com.newbarams.ajaja.module.user.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.user.application.model.Verification;
import com.newbarams.ajaja.module.user.application.port.in.VerifyCertificationUseCase;
import com.newbarams.ajaja.module.user.application.port.out.ApplyChangePort;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class VerifyCertificationService implements VerifyCertificationUseCase {
	private final RetrieveUserService retrieveUserService;
	private final ApplyChangePort applyChangePort;
	private final CacheUtil cacheUtil;

	@Override
	public void verify(Long userId, String certification) {
		User user = retrieveUserService.loadExistById(userId);
		Verification verification = cacheUtil.getEmailVerification(userId);
		verifyCertification(verification.getCertification(), certification);
		user.verified(verification.getTarget());
		applyChangePort.apply(user);
	}

	private void verifyCertification(String saved, String input) {
		if (!Objects.equals(saved, input)) {
			throw new AjajaException(CERTIFICATION_NOT_MATCH);
		}
	}
}
