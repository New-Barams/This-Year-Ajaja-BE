package com.newbarams.ajaja.module.remind.application;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.newbarams.ajaja.module.remind.domain.dto.RemindMailForm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SesSendEmailRemindService implements SendEmailRemindService {
	private final AmazonSimpleEmailService amazonSimpleEmailService;

	@Async
	@Override
	public void send(String email, String message, Long planId) {
		String feedbackLink = "http://localhost:8080/feedbacks/" + planId;

		RemindMailForm remindMailForm = createMail(email, message, feedbackLink);
		amazonSimpleEmailService.sendEmail(remindMailForm.toSesForm());
	}

	private RemindMailForm createMail(String email, String message, String feedbackLink) {
		return RemindMailForm.builder()
			.to(email)
			.subject("올해도 아좌좌 리마인드 메일 (테스트)")
			.content("내가 남긴 메세지:\n" + message + "\n ,피드백 링크: \n" + feedbackLink)
			.build();
	}
}
