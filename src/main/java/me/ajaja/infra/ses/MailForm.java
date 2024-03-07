package me.ajaja.infra.ses;

import static me.ajaja.infra.ses.HtmlEmailTemplate.MailType.*;
import static org.apache.commons.codec.CharEncoding.*;

import java.time.ZonedDateTime;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.BaseTime;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class MailForm {
	private static final String FROM_AJAJA = "no-reply@ajaja.me";

	private final String to;
	private final String subject;
	private final String content;

	public static MailForm verification(String to, String certification) {
		return MailForm.builder()
			.to(to)
			.subject(EMAIL_VERIFICATION.subject())
			.content(EMAIL_VERIFICATION.content(certification))
			.build();
	}

	public static MailForm remind(String to, String title, String message, String feedbackUrl) {
		ZonedDateTime deadLine = BaseTime.now().oneMonthLater();
		return MailForm.builder()
			.to(to)
			.subject(REMIND.subject(title))
			.content(REMIND.content(title, message, deadLine.getMonthValue(), deadLine.getDayOfMonth(), feedbackUrl))
			.build();
	}

	public static MailForm ajaja(String to, String title, Long ajajaCount, Long planId) {
		return MailForm.builder()
			.to(to)
			.subject(AJAJA.subject(title))
			.content(AJAJA.content(title, ajajaCount, planId))
			.build();
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
