package me.ajaja.infra.ses;

import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SesSendPlanRemindService {
	private final AmazonSimpleEmailService amazonSimpleEmailService;

	public int send(String email, String title, String message, String feedbackUrl) {
		MailForm mailForm = MailForm.remind(email, title, message, feedbackUrl);
		SendEmailResult response = amazonSimpleEmailService.sendEmail(mailForm.toSesForm());
		return response.getSdkHttpMetadata().getHttpStatusCode();
	}
}
