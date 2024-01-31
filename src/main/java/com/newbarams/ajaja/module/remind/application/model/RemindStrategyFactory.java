package com.newbarams.ajaja.module.remind.application.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.ses.SesSendPlanRemindService;
import com.newbarams.ajaja.module.remind.adapter.out.infra.SendAlimtalkAdapter;
import com.newbarams.ajaja.module.remind.application.port.out.SendRemindPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RemindStrategyFactory {
	private final SendAlimtalkAdapter alimtalkStrategy;
	private final SesSendPlanRemindService emailStrategy;

	public List<SendRemindPort> getAllStrategies() {
		return List.of(alimtalkStrategy, emailStrategy);
	}

	public SendRemindPort get(String remindType) {
		if (remindType.equals("KAKAO") || remindType.equals("BOTH")) {
			return alimtalkStrategy;
		}
		return emailStrategy;
	}
}
