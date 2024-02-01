package me.ajaja.module.remind.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.remind.application.model.RemindStrategyFactory;
import me.ajaja.module.remind.application.port.out.SendRemindPort;

@Service
@RequiredArgsConstructor
public class SchedulingRemindService {
	private static final String MORNING = "0 0 9 * 2-12 *";
	private static final String AFTERNOON = "0 0 13 * 2-12 *";
	private static final String EVENING = "0 0 20 * 2-12 *";

	private final RemindStrategyFactory remindStrategyFactory;

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
		TimeValue now = TimeValue.now();
		for (SendRemindPort remindStrategy : remindStrategyFactory.getAllStrategies()) {
			remindStrategy.send(remindTime, now);
		}
	}
}

