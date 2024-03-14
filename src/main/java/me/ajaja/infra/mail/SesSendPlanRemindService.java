package me.ajaja.infra.mail;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SesSendPlanRemindService {
	private final SmtpEmailSender emailSender;

	public int send(String email, String title, String message, String feedbackUrl) {
		Mail mail = Mail.remind(email, title, message, feedbackUrl);
		emailSender.send(mail);
		return 200; // todo: remove
	}
}
