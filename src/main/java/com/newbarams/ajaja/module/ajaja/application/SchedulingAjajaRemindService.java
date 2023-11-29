package com.newbarams.ajaja.module.ajaja.application;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.ajaja.domain.repository.AjajaRepository;
import com.newbarams.ajaja.module.remind.application.model.RemindableAjaja;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulingAjajaRemindService {
	private static final String WEEKLY_REMIND_TIME = "0 0 18 * * 2";
	private final AjajaRepository ajajaRepository;
	private final SendAjajaRemindService sendAjajaRemindService;

	@Scheduled(cron = WEEKLY_REMIND_TIME)
	public void scheduleMorningRemind() {
		List<RemindableAjaja> remindableAjajas = ajajaRepository.findRemindableAjaja();

		for (RemindableAjaja remindableAjaja : remindableAjajas) {
			String email = remindableAjaja.email();
			String title = remindableAjaja.title();
			Long planId = remindableAjaja.planId();
			Long ajajaCount = remindableAjaja.count();

			sendAjajaRemindService.send(email, title, ajajaCount, planId);
		}
	}
}
