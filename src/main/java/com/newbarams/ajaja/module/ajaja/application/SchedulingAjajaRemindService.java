package com.newbarams.ajaja.module.ajaja.application;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.ajaja.domain.AjajaQueryRepository;
import com.newbarams.ajaja.module.remind.application.model.RemindableAjaja;
import com.newbarams.ajaja.module.remind.application.port.out.SaveRemindPort;
import com.newbarams.ajaja.module.remind.domain.Info;
import com.newbarams.ajaja.module.remind.domain.Remind;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SchedulingAjajaRemindService {
	private static final String WEEKLY_REMIND_TIME = "0 0 18 * * 2";

	private final AjajaQueryRepository ajajaQueryRepository;
	private final SendAjajaRemindService sendAjajaRemindService;
	private final SaveRemindPort saveRemindPort;

	@Scheduled(cron = WEEKLY_REMIND_TIME)
	public void scheduleMorningRemind() {
		List<RemindableAjaja> remindableAjajas = ajajaQueryRepository.findRemindableAjaja();
		TimeValue time = new TimeValue();
		for (RemindableAjaja remindableAjaja : remindableAjajas) {
			String email = remindableAjaja.email();
			String title = remindableAjaja.title();
			Long planId = remindableAjaja.planId();
			Long ajajaCount = remindableAjaja.count();

			sendAjajaRemindService.send(email, title, ajajaCount, planId);
			saveAjajaRemind(remindableAjaja, time);
		}
	}

	private void saveAjajaRemind(RemindableAjaja remindableAjaja, TimeValue time) {
		String message = createAjajaMessage(remindableAjaja.title(), remindableAjaja.count());
		Info info = new Info(message);
		Remind remind = Remind.ajaja(remindableAjaja.userId(), remindableAjaja.planId(), info, time.getMonth(),
			time.getDate());
		saveRemindPort.save(remind);
	}

	private String createAjajaMessage(String title, Long count) {
		return "지난 주에 " + title + " 계획 계획을 " + count + "명이나 응원했어요";
	}
}
