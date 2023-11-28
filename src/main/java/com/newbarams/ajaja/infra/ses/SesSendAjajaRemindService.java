package com.newbarams.ajaja.infra.ses;

import static com.newbarams.ajaja.infra.ses.template.AjajaRemindTemplate.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.newbarams.ajaja.module.ajaja.application.SendAjajaRemindService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SesSendAjajaRemindService implements SendAjajaRemindService {
	private final AmazonSimpleEmailService amazonSimpleEmailService;

	@Async
	@Override
	public void send(String email, String title, Long ajajaCount, Long planId) {
		MailForm remindMailForm = createMail(email, title, ajajaCount, planId);
		amazonSimpleEmailService.sendEmail(remindMailForm.toSesForm());
	}

	private MailForm createMail(String email, String title, Long ajajaCount, Long planId) {
		return MailForm.builder()
			.to(email)
			.subject("[올해도 아좌좌] \"%s\" 계획이 응원을 받았어요!".formatted(title))
			.content(html.formatted(title, ajajaCount, planId))
			.build();
	}
}
