package com.newbarams.ajaja.infra.ses;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.newbarams.ajaja.module.remind.application.port.out.SendPlanInfoRemindPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class SesSendPlanRemindAdapter implements SendPlanInfoRemindPort {
	private static final String FEEDBACK_URL = "https://www.ajaja.me/plans/";

	private final AmazonSimpleEmailService amazonSimpleEmailService;

	@Async
	@Override
	public void send(String email, String title, String message, Long planId) {
		MailForm mailForm = MailForm.remind(email, title, message, FEEDBACK_URL + planId);
		amazonSimpleEmailService.sendEmail(mailForm.toSesForm());
		log.info("[SES] Remind Sent To : {}", email);
	}
}
