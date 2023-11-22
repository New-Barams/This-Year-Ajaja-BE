package com.newbarams.ajaja.module.remind.application;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.module.feedback.service.CreateFeedbackService;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.repository.PlanQueryRepository;
import com.newbarams.ajaja.module.plan.repository.PlanRepository;
import com.newbarams.ajaja.module.remind.domain.dto.Response;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulingRemindService {
	private final CreateRemindService createRemindService;
	private final SendEmailRemindService sendEmailRemindService;
	private final CreateFeedbackService createFeedbackService;
	private final PlanQueryRepository planQueryRepository;
	private final PlanRepository planRepository;

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
		List<Response> remindInfos = loadAllRemindablePlan(remindTime);
		sendEmail(remindInfos);
	}

	private List<Response> loadAllRemindablePlan(String remindTime) {
		return planQueryRepository.findAllRemindablePlan(remindTime);
	}

	private void sendEmail(List<Response> remindInfos) {
		for (Response remindInfo : remindInfos) {
			if (remindInfo.getRemindTerm() == 1) {
				remindInfo.setIdx(remindInfo.getIdx() / remindInfo.getRemindTerm());
			}

			Plan plan = planRepository.findById(remindInfo.getPlanId()).orElseThrow(
				() -> new AjajaException(NOT_FOUND_PLAN));

			String message = plan.getMessages().get(remindInfo.getIdx()).getContent();
			Long feedbackId = createFeedbackService.createFeedback(remindInfo.getUserId(), remindInfo.getPlanId());
			// todo : 쿼리dsl 오류로 인한 임시 구현 코드 (나중에 지우기)
			sendEmailRemindService.send(remindInfo.getEmail(), message, feedbackId);

			createRemindService.createRemind(remindInfo.getUserId(), remindInfo.getPlanId(), message,
				remindInfo.getInfo());
		}
	}
}
