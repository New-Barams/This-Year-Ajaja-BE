package me.ajaja.infra.mail;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.module.user.application.port.out.SendCertificationPort;

@Slf4j
@Component
@RequiredArgsConstructor
class SesSendCertificationPort implements SendCertificationPort {
	private final SmtpEmailSender emailSender;

	@Override
	public void send(String email, String certification) {
		Mail mail = Mail.verification(email, certification);
		emailSender.send(mail);
		log.info("[SMTP] Verification Sent To : {}", email);
	}
}
