package me.ajaja.infra.mail;

import static me.ajaja.infra.mail.HtmlEmailTemplate.MailType.*;

import java.time.ZonedDateTime;
import java.util.Objects;

import lombok.Getter;
import me.ajaja.global.common.BaseTime;

@Getter
class Mail {
	private final String to;
	private final String subject;
	private final String content;

	private Mail(String to, String subject, String content) {
		Objects.requireNonNull(to, "target email cannot be null");
		Objects.requireNonNull(subject, "subject cannot be null");
		Objects.requireNonNull(content, "content cannot be null");
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	public static Mail verification(String to, String certification) {
		return new Mail(to, EMAIL_VERIFICATION.subject(), EMAIL_VERIFICATION.content(certification));
	}

	public static Mail remind(String to, String title, String message, String feedbackUrl) {
		ZonedDateTime deadLine = BaseTime.now().oneMonthLater();
		return new Mail(
			to,
			REMIND.subject(title),
			REMIND.content(title, message, deadLine.getMonthValue(), deadLine.getDayOfMonth(), feedbackUrl)
		);
	}

	public static Mail ajaja(String to, String title, Long ajajaCount, Long planId) {
		return new Mail(to, AJAJA.subject(title), AJAJA.content(title, ajajaCount, planId));
	}
}
