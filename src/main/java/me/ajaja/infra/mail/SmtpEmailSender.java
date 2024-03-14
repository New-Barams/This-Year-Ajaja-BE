package me.ajaja.infra.mail;

import static org.eclipse.jdt.internal.compiler.util.Util.*;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
class SmtpEmailSender {
	private static final String FROM_AJAJA = "no-reply@ajaja.me";

	private final JavaMailSender javaMailSender;

	@Async
	public void send(Mail mail) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, UTF_8);
		setContents(messageHelper, mail);
		javaMailSender.send(message);
	}

	private void setContents(MimeMessageHelper messageHelper, Mail mail) {
		try {
			messageHelper.setTo(mail.getTo());
			messageHelper.setFrom(FROM_AJAJA);
			messageHelper.setSubject(mail.getSubject());
			messageHelper.setText(mail.getContent(), true);
		} catch (MessagingException exception) {
			throw new AjajaException(ErrorCode.AJAJA_SERVER_ERROR);
		}
	}
}
