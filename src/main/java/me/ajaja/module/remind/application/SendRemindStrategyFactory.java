package me.ajaja.module.remind.application;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class SendRemindStrategyFactory {
	private final SendAlimtalkRemindStrategy alimtalkStrategy;
	private final SendEmailRemindStrategy emailStrategy;

	public List<SendRemindStrategy> getStrategies() {
		return List.of(alimtalkStrategy, emailStrategy);
	}

	public SendRemindStrategy get(String remindType) {
		return "EMAIL".equals(remindType) ? emailStrategy : alimtalkStrategy;
	}
}
