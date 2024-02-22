package me.ajaja.module.remind.application.model;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.ajaja.infra.ses.SesSendPlanRemindService;
import me.ajaja.module.remind.adapter.out.infra.SendAlimtalkAdapter;
import me.ajaja.module.remind.application.port.out.SendRemindPort;

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
