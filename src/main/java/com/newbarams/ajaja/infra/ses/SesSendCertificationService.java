package com.newbarams.ajaja.infra.ses;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.newbarams.ajaja.module.user.application.SendCertificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class SesSendCertificationService implements SendCertificationService {
	private final AmazonSimpleEmailService amazonSimpleEmailService;

	@Async
	@Override
	public void send(String email, String certification) {
		MailForm mailForm = MailForm.verification(email, certification);
		amazonSimpleEmailService.sendEmail(mailForm.toSesForm());
		log.info("[SES] Verification Sent To : {}", email);
	}
}
