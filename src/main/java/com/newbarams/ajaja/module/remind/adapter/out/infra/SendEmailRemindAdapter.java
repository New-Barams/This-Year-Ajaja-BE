package com.newbarams.ajaja.module.remind.adapter.out.infra;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.ses.SesSendPlanRemindService;
import com.newbarams.ajaja.module.remind.application.port.out.SendEmailRemindPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SendEmailRemindAdapter implements SendEmailRemindPort {
	private final SesSendPlanRemindService sendPlanRemindService;

	@Override
	public void send(String email, String title, String message, String feedbackUrl) {
		sendPlanRemindService.send(email, title, message, feedbackUrl);
	}
}
