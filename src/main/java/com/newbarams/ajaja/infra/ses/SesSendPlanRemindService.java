package com.newbarams.ajaja.infra.ses;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SesSendPlanRemindService {
	private final AmazonSimpleEmailService amazonSimpleEmailService;

	@Async
	public void send(String email, String title, String message, String feedbackUrl) {
		MailForm mailForm = MailForm.remind(email, title, message, feedbackUrl);
		amazonSimpleEmailService.sendEmail(mailForm.toSesForm());
		log.info("[SES] Remind Sent To : {}", email);
	}
}
