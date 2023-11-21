package com.newbarams.ajaja.module.remind.application;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.newbarams.ajaja.module.feedback.service.CreateFeedbackService;
import com.newbarams.ajaja.module.plan.repository.PlanQueryRepository;
import com.newbarams.ajaja.module.remind.domain.dto.GetRemindablePlan;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulingRemindService {
	private final SendEmailRemindService sendEmailRemindService;
	private final CreateFeedbackService createFeedbackService;
	private final PlanQueryRepository planQueryRepository;
	private final CreateRemindService createRemindService;

	@Scheduled(cron = "0 0 9 * * *")
	public void scheduleMorningRemind() {
		String remindTime = "MORNING";

		executeRemindProcess(remindTime);
	}

	@Scheduled(cron = "0 0 13 * * *")
	public void scheduleAfternoonRemind() {
		String remindTime = "AFTERNOON";

		executeRemindProcess(remindTime);
	}

	@Scheduled(cron = "0 0 22 * * *")
	public void scheduleEveningRemind() {
		String remindTime = "EVENING";

		executeRemindProcess(remindTime);
	}

	private void executeRemindProcess(String remindTime) {
		List<GetRemindablePlan.Response> remindInfos = loadAllRemindablePlan(remindTime);
		sendEmail(remindInfos);
	}

	private List<GetRemindablePlan.Response> loadAllRemindablePlan(String remindTime) {
		return planQueryRepository.findAllRemindablePlan(remindTime);
	}

	private void sendEmail(List<GetRemindablePlan.Response> remindInfos) {
		for (GetRemindablePlan.Response remindInfo : remindInfos) {
			Long feedbackId = createFeedbackService.createFeedback(remindInfo.userId(), remindInfo.planId());

			sendEmailRemindService.send(remindInfo.email(), remindInfo.message(), feedbackId);

			createRemindService.createRemind(remindInfo.userId(), remindInfo.planId(), remindInfo.message(),
				remindInfo.info());
		}
	}
}
