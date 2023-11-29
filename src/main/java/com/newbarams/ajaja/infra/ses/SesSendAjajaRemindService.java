package com.newbarams.ajaja.infra.ses;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.newbarams.ajaja.module.ajaja.application.SendAjajaRemindService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class SesSendAjajaRemindService implements SendAjajaRemindService {
	private final AmazonSimpleEmailService amazonSimpleEmailService;

	@Async
	@Override
	public void send(String email, String title, Long ajajaCount, Long planId) {
		MailForm mailForm = MailForm.ajaja(email, title, ajajaCount, planId);
		amazonSimpleEmailService.sendEmail(mailForm.toSesForm());
		log.info("[SES] Ajaja Sent To : {}", email);
	}
}
