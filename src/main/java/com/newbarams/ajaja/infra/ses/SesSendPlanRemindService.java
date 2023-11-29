package com.newbarams.ajaja.infra.ses;

import static com.newbarams.ajaja.infra.ses.template.PlanRemindTemplate.*;

import java.time.ZonedDateTime;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.remind.application.SendPlanRemindService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class SesSendPlanRemindService implements SendPlanRemindService {
	private static final String FEEDBACK_LINK = "https://this-year-ajaja-fe.vercel.app/plans/";
	private final AmazonSimpleEmailService amazonSimpleEmailService;

	@Async
	@Override
	public void send(String email, String title, String message, Long planId) {
		String feedbackLink = FEEDBACK_LINK + planId;

		MailForm remindMailForm = createMail(email, title, message, feedbackLink);
		amazonSimpleEmailService.sendEmail(remindMailForm.toSesForm());
	}

	private MailForm createMail(String email, String title, String message, String feedbackLink) {
		ZonedDateTime deadLine = new TimeValue().getOneMonthLater();

		return MailForm.builder()
			.to(email)
			.subject("[올해도 아좌좌] \"%s\" 계획에 대한 리마인드 메일입니다.".formatted(title))
			.content(html.formatted(
				title, message, deadLine.getMonthValue(), deadLine.getDayOfMonth(), feedbackLink
			))
			.build();
	}
}
