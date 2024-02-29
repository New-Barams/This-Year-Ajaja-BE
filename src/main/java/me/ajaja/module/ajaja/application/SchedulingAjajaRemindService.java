package me.ajaja.module.ajaja.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class SchedulingAjajaRemindService {
	private static final String WEEKLY_REMIND_TIME = "0 0 18 * * 1";

	private final SendAjajaStrategyFactory sendAjajaStrategyFactory;

	@Scheduled(cron = WEEKLY_REMIND_TIME)
	public void scheduleAjajaRemind() {
		sendAjajaStrategyFactory.getStrategies()
			.forEach(SendAjajaStrategy::send);
	}
}
