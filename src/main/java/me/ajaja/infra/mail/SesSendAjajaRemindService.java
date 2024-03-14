package me.ajaja.infra.mail;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SesSendAjajaRemindService {
	private final SmtpEmailSender emailSender;

	public int send(String email, String title, Long count, Long targetId) {
		Mail mail = Mail.ajaja(email, title, count, targetId);
		emailSender.send(mail);
		return 200; // todo: remove
	}
}
