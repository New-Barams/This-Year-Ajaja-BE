package com.newbarams.ajaja.module.remind.application;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.remind.application.port.out.FindRemindablePlanPort;
import com.newbarams.ajaja.module.remind.application.port.out.SendPlanInfoRemindPort;
import com.newbarams.ajaja.module.remind.domain.Remind;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SchedulingRemindService {
	private static final String MORNING = "0 0 9 * 2-12 *";
	private static final String AFTERNOON = "0 0 13 * 2-12 *";
	private static final String EVENING = "0 0 22 * 2-12 *";

	private final FindRemindablePlanPort findRemindablePlanPort;
	private final SendPlanInfoRemindPort sendPlanInfoRemindPort;
	private final CreateRemindService createRemindService;

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
		TimeValue time = new TimeValue();
		List<Remind> remindablePlans = findRemindablePlanPort.findAllRemindablePlan(remindTime, time);
		sendEmail(remindablePlans, time);
	}

	private void sendEmail(List<Remind> remindMessageInfos, TimeValue time) {
		for (Remind remindInfo : remindMessageInfos) {

			sendPlanInfoRemindPort.send(
				remindInfo.getEmail(),
				remindInfo.getTitle(),
				remindInfo.getMessage(),
				remindInfo.getPlanId()
			);

			createRemindService.create(remindInfo, time);
		}
	}
}
