package com.newbarams.ajaja.infra.ses;

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
	public void send(String email, Long ajajaNumber) {
		MailForm remindMailForm = createMail(email, ajajaNumber);
		amazonSimpleEmailService.sendEmail(remindMailForm.toSesForm());
	}

	private MailForm createMail(String email, Long ajajaNumber) {
		return MailForm.builder()
			.to(email)
			.subject("올해도 아좌좌 응원 리마인드 메일 (테스트)")
			.content("총" + ajajaNumber + "명의 유저가 당신의 계획을 응원하고 있어요!")
			.build();
	}
}
