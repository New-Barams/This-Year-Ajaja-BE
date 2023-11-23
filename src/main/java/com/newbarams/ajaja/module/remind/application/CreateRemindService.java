package com.newbarams.ajaja.module.remind.application;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ListIterator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.remind.domain.Info;
import com.newbarams.ajaja.module.remind.domain.Period;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.repository.RemindRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateRemindService {
	private final RemindRepository remindRepository;
	private final Instant instant = Instant.now();
	private final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

	public void createRemind(Long userId, Long planId, String message, RemindInfo remindInfo) {
		int remindMonth = zonedDateTime.getMonthValue();
		int remindTime = remindInfo.getRemindTime();

		Instant time = parseInstant(remindMonth, remindInfo.getRemindDate(), remindTime);

		Info info = new Info(message);

		Period period = new Period(time, time.plus(31, ChronoUnit.DAYS));
		Remind remind = Remind.plan(userId, planId, info, period);

		remindRepository.save(remind);
	}

	private Instant parseInstant(int remindMonth, int remindDate, int remindTime) {
		return Instant.parse(
			"2024-" + String.format("%02d", remindMonth) + "-" + String.format("%02d", remindDate) + "T"
				+ String.format("%02d", remindTime) + ":00:00Z");
	}
}
