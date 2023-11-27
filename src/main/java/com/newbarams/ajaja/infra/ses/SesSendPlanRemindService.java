package com.newbarams.ajaja.infra.ses;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.newbarams.ajaja.module.remind.application.SendPlanRemindService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SesSendPlanRemindService implements SendPlanRemindService {
	private final AmazonSimpleEmailService amazonSimpleEmailService;

	@Async
	@Override
	public void send(String email, String message, Long feedbackId) {
		String feedbackLink = "http://feedbacks/" + feedbackId;

		MailForm remindMailForm = createMail(email, message, feedbackLink);
		amazonSimpleEmailService.sendEmail(remindMailForm.toSesForm());
	}

	private MailForm createMail(String email, String message, String feedbackLink) {
		return MailForm.builder()
			.to(email)
			.subject("올해도 아좌좌 리마인드 메일 (테스트)")
			.content("내가 남긴 메세지:\n" + message + "\n ,피드백 링크: \n" + feedbackLink)
			.build();
	}
}
