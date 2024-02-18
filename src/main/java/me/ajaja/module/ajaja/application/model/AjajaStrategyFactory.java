package me.ajaja.module.ajaja.application.model;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.ajaja.infra.ses.SesSendAjajaRemindService;

@Component
@RequiredArgsConstructor
public class AjajaStrategyFactory {
	private final SendAlimtalkStrategy sendAlimtalkStrategy;
	private final SesSendAjajaRemindService sendEmailStrategy;

	public List<SendAjajaStrategy> getAllStrategies() {
		return List.of(sendAlimtalkStrategy, sendEmailStrategy);
	}
}
