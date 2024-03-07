package me.ajaja.module.ajaja.application;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class SendAjajaStrategyFactory {
	private final SendAlimtalkAjajaStrategy sendAlimtalkAjajaStrategy;
	private final SendEmailAjajaStrategy sendEmailAjajaStrategy;

	public List<SendAjajaStrategy> getStrategies() {
		return List.of(sendAlimtalkAjajaStrategy, sendEmailAjajaStrategy);
	}
}
