package com.newbarams.ajaja.module.ajaja.application;

import static com.newbarams.ajaja.module.user.domain.QUser.*;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.ajaja.domain.repository.AjajaQueryRepository;
import com.querydsl.core.Tuple;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulingAjajaRemindService {
	private static final String WEEKLY_REMIND_TIME = "0 0 18 * * 2";
	private final AjajaQueryRepository ajajaQueryRepository;
	private final SendAjajaRemindService sendAjajaRemindService;

	@Scheduled(cron = WEEKLY_REMIND_TIME)
	public void scheduleMorningRemind() {
		List<Tuple> remindableAjaja = ajajaQueryRepository.findRemindableAjaja();

		for (Tuple tuple : remindableAjaja) {
			String email = tuple.get(user.email.email);
			Long ajajaNumber = tuple.get(user.email.email.count());
			sendAjajaRemindService.send(email, ajajaNumber);
		}
	}
}
