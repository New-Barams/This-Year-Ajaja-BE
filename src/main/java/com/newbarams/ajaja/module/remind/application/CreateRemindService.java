package com.newbarams.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.Info;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.RemindRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateRemindService {
	private final RemindRepository remindRepository;

	public void createRemind(Plan plan, TimeValue time) {
		Info info = new Info(plan.getMessage(time.getMonth()));
		Remind remind = Remind.plan(plan.getUserId(), plan.getId(), info, time.getMonth(), time.getDate());
		remindRepository.save(remind);
	}
}
