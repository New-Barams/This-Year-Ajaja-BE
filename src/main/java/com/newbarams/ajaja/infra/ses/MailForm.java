package com.newbarams.ajaja.infra.ses;

import static com.newbarams.ajaja.infra.ses.HtmlEmailTemplate.MailType.*;
import static org.apache.commons.codec.CharEncoding.*;

import java.time.ZonedDateTime;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.newbarams.ajaja.global.common.TimeValue;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class MailForm {
	private static final String FROM_AJAJA = "no-reply@ajaja.me";

	private final String to;
	private final String subject;
	private final String content;

	public static MailForm verification(String to, String certification) {
		return new MailForm(
			to,
			EMAIL_VERIFICATION.subject(),
			EMAIL_VERIFICATION.content(certification)
		);
	}

	public static MailForm remind(String to, String title, String message, String feedbackUrl) {
		ZonedDateTime deadLine = TimeValue.now().oneMonthLater();
		return new MailForm(
			to,
			REMIND.subject(title),
			REMIND.content(title, message, deadLine.getMonthValue(), deadLine.getDayOfMonth(), feedbackUrl)
		);
	}

	public static MailForm ajaja(String to, String title, Long ajajaCount, Long planId) {
		return new MailForm(
			to,
			AJAJA.subject(title),
			AJAJA.content(title, ajajaCount, planId)
		);
	}

	public SendEmailRequest toSesForm() {
		return new SendEmailRequest()
			.withSource(FROM_AJAJA)
			.withDestination(generateDestination())
			.withMessage(generateMessage());
	}

	private Destination generateDestination() {
		return new Destination().withToAddresses(to);
	}

	private Message generateMessage() {
		return new Message()
			.withSubject(withDefaultCharset(subject))
			.withBody(new Body().withHtml(withDefaultCharset(content)));
	}

	private Content withDefaultCharset(String text) {
		return new Content()
			.withCharset(UTF_8)
			.withData(text);
	}
}
