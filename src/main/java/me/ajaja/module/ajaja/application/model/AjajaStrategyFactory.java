package me.ajaja.module.ajaja.application.model;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AjajaStrategyFactory {
	private final SendAlimtalkStrategy sendAlimtalkStrategy;
	private final SendEmailStrategy sendEmailStrategy;

	public List<SendAjajaStrategy> getAllStrategies() {
		return List.of(sendAlimtalkStrategy, sendEmailStrategy);
	}
}
