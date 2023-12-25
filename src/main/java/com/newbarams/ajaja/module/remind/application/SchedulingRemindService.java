package com.newbarams.ajaja.module.remind.application;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.feedback.application.CreateFeedbackService;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.remind.application.model.RemindMessageInfo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SchedulingRemindService {
	private final CreateRemindService createRemindService;
	private final SendPlanRemindService sendPlanRemindService;
	private final CreateFeedbackService createFeedbackService;
	private final PlanQueryRepository planQueryRepository;

	@Scheduled(cron = "0 0 9 * * *")
	public void scheduleMorningRemind() {
		sendRemindsOnScheduledTime("MORNING");
	}

	@Scheduled(cron = "0 0 13 * * *")
	public void scheduleAfternoonRemind() {
		sendRemindsOnScheduledTime("AFTERNOON");
	}

	@Scheduled(cron = "0 0 22 * * *")
	public void scheduleEveningRemind() {
		sendRemindsOnScheduledTime("EVENING");
	}

	private void sendRemindsOnScheduledTime(String remindTime) {
		TimeValue time = new TimeValue();
		List<RemindMessageInfo> remindablePlans = planQueryRepository.findAllRemindablePlan(remindTime, time);
		sendEmail(remindablePlans, time); // todo : method 수정
	}

	private void sendEmail(List<RemindMessageInfo> remindMessageInfos, TimeValue time) {
		for (RemindMessageInfo remindInfo : remindMessageInfos) {
			Plan plan = remindInfo.plan();
			createFeedbackService.create(plan.getUserId(), plan.getId());

			sendPlanRemindService.send(
				remindInfo.email(),
				plan.getPlanTitle(),
				plan.getMessage(time.getMonth()),
				plan.getId()
			);

			createRemindService.createRemind(plan, time);
		}
	}
}
