package com.newbarams.ajaja.module.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.module.user.application.port.in.SendVerificationEmailUseCase;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class SendVerificationEmailService implements SendVerificationEmailUseCase {
	private final SendCertificationService sendCertificationService;
	private final RetrieveUserService retrieveUserService;
	private final CacheUtil cacheUtil;

	@Override
	public void sendVerification(Long userId, String email) {
		validateEmail(userId, email);
		String certification = RandomCertificationGenerator.generate();
		sendCertificationService.send(email, certification);
		cacheUtil.saveEmailVerification(userId, email, certification);
	}

	private void validateEmail(Long id, String email) {
		User user = retrieveUserService.loadExistById(id);
		user.validateEmail(email);
	}
}
