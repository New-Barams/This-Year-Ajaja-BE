package com.newbarams.ajaja.module.remind.application;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.newbarams.ajaja.global.common.error.ErrorCode;
import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.module.feedback.service.CreateFeedbackService;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.repository.RemindQueryRepository;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulingRemindService {
	private final RemindQueryRepository remindQueryRepository;
	private final SendEmailRemindService sendEmailRemindService;
	private final UserRepository userRepository;
	private final CreateFeedbackService createFeedbackService;

	@Scheduled(cron = "0 0 9 * * *")
	public void scheduleMorningRemind() {
		int remindHour = 03;

		executeRemind(remindHour);
	}

	@Scheduled(cron = "0 0 13 * * *")
	public void scheduleAfternoonRemind() {
		int remindHour = 13;

		executeRemind(remindHour);
	}

	@Scheduled(cron = "0 0 22 * * *")
	public void scheduleEveningRemind() {
		int remindHour = 22;

		executeRemind(remindHour);
	}

	private void executeRemind(int remindHour) {
		List<Remind> reminds = findReminds(remindHour);
		sendEmail(reminds);
	}

	private List<Remind> findReminds(int remindHour) {
		return remindQueryRepository.findRemindByHour(remindHour);
	}

	private void sendEmail(List<Remind> reminds) {
		for (Remind remind : reminds) {
			User remindUser = userRepository.findById(remind.getUserId()).orElseThrow(
				() -> new AjajaException(ErrorCode.USER_NOT_FOUND));

			Long feedbackId = createFeedbackService.createFeedback(remind.getUserId(), remind.getPlanId());

			sendEmailRemindService.send("yamsang2002@naver.com", remind.getInfo().getContent(), feedbackId);
		}
	}
}
