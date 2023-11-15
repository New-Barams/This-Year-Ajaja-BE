package com.newbarams.ajaja.module.remind.application;

import java.time.Instant;
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

	public void createRemind(Long userId, Long planId, List<Message> messages, RemindInfo remindInfo) {
		ListIterator<Message> messageIterator = messages.listIterator();

		int totalPeriod = remindInfo.getRemindTotalPeriod();
		int remindMonth = remindInfo.getRemindMonth();

		while (remindMonth <= totalPeriod) {
			Info info = new Info(messageIterator.next().getContent());
			int remindTime = remindInfo.getRemindTime(remindInfo.getRemindTime().name());

			Instant time = parseInstant(remindInfo.getRemindDate(), remindMonth, remindTime);

			Period period = new Period(time, time.plus(31, ChronoUnit.DAYS));
			Remind remind = Remind.plan(userId, planId, info, period);

			remindRepository.save(remind);
			remindMonth += remindInfo.getRemindTerm();
		}
	}

	private Instant parseInstant(int remindDate, int remindMonth, int remindTime) {
		return Instant.parse(
			"2024-" + String.format("%02d", remindMonth) + "-" + String.format("%02d", remindDate) + "T"
				+ String.format("%02d", remindTime) + ":00:00Z");
	}
}
