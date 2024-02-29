package me.ajaja.module.ajaja.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.ajaja.application.model.AjajaStrategyFactory;
import me.ajaja.module.ajaja.application.model.SendAjajaStrategy;

@Service
@RequiredArgsConstructor
@Transactional
public class SchedulingAjajaRemindService {
	private static final String WEEKLY_REMIND_TIME = "0 0 18 * * 1";

	private final AjajaStrategyFactory ajajaStrategyFactory;

	@Scheduled(cron = WEEKLY_REMIND_TIME)
	public void scheduleAjajaRemind() {
		for (SendAjajaStrategy strategy : ajajaStrategyFactory.getAllStrategies()) {
			strategy.send(TimeValue.now());
		}
	}
}
