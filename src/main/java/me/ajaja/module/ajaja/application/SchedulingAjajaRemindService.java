package me.ajaja.module.ajaja.application;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.remind.application.model.RemindableAjaja;
import me.ajaja.module.remind.application.port.out.SaveRemindPort;
import me.ajaja.module.remind.domain.Remind;

@Service
@RequiredArgsConstructor
@Transactional
public class SchedulingAjajaRemindService {
	private static final String WEEKLY_REMIND_TIME = "0 0 18 * * 1";

	private final AjajaQueryRepository ajajaQueryRepository;
	private final SendAjajaRemindService sendAjajaRemindService;
	private final SaveRemindPort saveRemindPort;

	@Scheduled(cron = WEEKLY_REMIND_TIME)
	public void scheduleMorningRemind() {
		List<RemindableAjaja> remindableAjajas = ajajaQueryRepository.findRemindableAjaja();
		TimeValue now = TimeValue.now();
		for (RemindableAjaja remindableAjaja : remindableAjajas) {
			String email = remindableAjaja.email();
			String title = remindableAjaja.title();
			Long planId = remindableAjaja.planId();
			Long ajajaCount = remindableAjaja.count();

			sendAjajaRemindService.send(email, title, ajajaCount, planId);
			saveAjajaRemind(remindableAjaja, now);
		}
	}

	private void saveAjajaRemind(RemindableAjaja remindableAjaja, TimeValue time) {
		String message = createAjajaMessage(remindableAjaja.title(), remindableAjaja.count());
		Remind remind = Remind.ajaja(remindableAjaja.userId(), remindableAjaja.planId(), message, time.getMonth(),
			time.getDate());
		saveRemindPort.save(remind);
	}

	private String createAjajaMessage(String title, Long count) {
		return "지난 주에 " + title + " 계획 계획을 " + count + "명이나 응원했어요";
	}
}
