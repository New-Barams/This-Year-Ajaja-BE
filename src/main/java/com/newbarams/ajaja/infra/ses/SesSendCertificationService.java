package com.newbarams.ajaja.infra.ses;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.newbarams.ajaja.module.user.application.SendCertificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
class SesSendCertificationService implements SendCertificationService {
	private final AmazonSimpleEmailService amazonSimpleEmailService;

	@Async
	@Override
	public void send(String email, String certification) {
		MailForm mailForm = createMail(email, certification);
		amazonSimpleEmailService.sendEmail(mailForm.toSesForm());
		log.info("[SES] Email Sent To : {}", email);
	}

	private MailForm createMail(String email, String certification) {
		return MailForm.builder()
			.to(email)
			.content("다음 인증 번호를 입력해주세요.\n인증번호 : " + certification)
			.subject("올해도 아좌좌 서비스 이메일 인증 메일입니다.")
			.build();
	}
}
