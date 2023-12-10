package com.newbarams.ajaja.module.remind.application;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.remind.domain.Info;
import com.newbarams.ajaja.module.remind.domain.Period;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.RemindRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateRemindService {
	private final RemindRepository remindRepository;

	public void createRemind(Long userId, Long planId, String message, RemindInfo remindInfo) {
		int remindMonth = new TimeValue().getMonth();
		int remindTime = remindInfo.getRemindTime();

		Instant time = new TimeValue().parseInstant(remindMonth, remindInfo.getRemindDate(), remindTime);

		Info info = new Info(message);
		Period period = new Period(time, time.plus(31, ChronoUnit.DAYS));
		Remind remind = Remind.plan(userId, planId, info, period);

		remindRepository.save(remind);
	}
}
