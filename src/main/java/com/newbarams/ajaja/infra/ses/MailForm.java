package com.newbarams.ajaja.infra.ses;

import static org.apache.commons.codec.CharEncoding.*;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
class MailForm {
	private static final String FROM_AJAJA = "no-reply@ajaja.me";

	private final String to;
	private final String subject;
	private final String content;

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
