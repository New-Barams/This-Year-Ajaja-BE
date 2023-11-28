package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SendVerificationEmailService {
	private final SendCertificationService sendCertificationService;
	private final RetrieveUserService retrieveUserService;
	private final CacheUtil cacheUtil;

	public void sendVerification(Long userId, String email) {
		validateEmail(userId, email);
		String certification = RandomCertificationGenerator.generate();
		sendCertificationService.send(email, certification);
		cacheUtil.saveEmailVerification(userId, email, certification);
	}

	private void validateEmail(Long id, String email) {
		User user = retrieveUserService.loadExistUserById(id);
		user.validateEmail(email);
	}
}
