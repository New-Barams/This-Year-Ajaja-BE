package com.newbarams.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.remind.domain.Info;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.RemindRepository;
import com.newbarams.ajaja.module.remind.dto.RemindMessageInfo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateRemindService {
	private final RemindRepository remindRepository;

	public void createRemind(RemindMessageInfo remindInfo) {
		Info info = new Info(remindInfo.getMessage());
		Remind remind = Remind.plan(remindInfo.getUserId(), remindInfo.getPlanId(), info, remindInfo.getRemindMonth(),
			remindInfo.getRemindDate());
		remindRepository.save(remind);
	}
}
