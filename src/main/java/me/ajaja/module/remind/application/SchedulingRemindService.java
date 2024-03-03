package me.ajaja.module.remind.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;

@Service
@RequiredArgsConstructor
class SchedulingRemindService {
	private static final String MORNING = "0 0 9 * 2-12 *";
	private static final String AFTERNOON = "0 0 13 * 2-12 *";
	private static final String EVENING = "0 0 20 * 2-12 *";

	private final SendRemindStrategyFactory sendRemindStrategyFactory;

	@Scheduled(cron = MORNING)
	public void scheduleMorningRemind() {
		sendRemindsOnScheduledTime("MORNING");
	}

	@Scheduled(cron = AFTERNOON)
	public void scheduleAfternoonRemind() {
		sendRemindsOnScheduledTime("AFTERNOON");
	}

	@Scheduled(cron = EVENING)
	public void scheduleEveningRemind() {
		sendRemindsOnScheduledTime("EVENING");
	}

	private void sendRemindsOnScheduledTime(String remindTime) {
		sendRemindStrategyFactory.getStrategies()
			.forEach(strategy -> strategy.send(remindTime, TimeValue.now()));
	}
}

