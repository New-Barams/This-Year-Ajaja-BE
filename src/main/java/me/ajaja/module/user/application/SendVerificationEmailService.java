package me.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.cache.CacheUtil;
import me.ajaja.module.user.application.port.in.SendVerificationEmailUseCase;
import me.ajaja.module.user.application.port.out.SendCertificationPort;
import me.ajaja.module.user.domain.User;

@Service
@Transactional
@RequiredArgsConstructor
class SendVerificationEmailService implements SendVerificationEmailUseCase {
	private final SendCertificationPort sendCertificationPort;
	private final RetrieveUserService retrieveUserService;
	private final CacheUtil cacheUtil;

	@Override
	public void sendVerification(Long userId, String email) {
		validateEmail(userId, email);
		String certification = RandomCertificationGenerator.generate();
		sendCertificationPort.send(email, certification);
		cacheUtil.saveEmailVerification(userId, email, certification);
	}

	private void validateEmail(Long id, String email) {
		User user = retrieveUserService.loadExistById(id);
		user.validateEmail(email);
	}
}
