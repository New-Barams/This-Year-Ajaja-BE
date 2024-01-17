package com.newbarams.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.adapter.out.persistence.SaveRemindAdapter;
import com.newbarams.ajaja.module.remind.domain.Info;
import com.newbarams.ajaja.module.remind.domain.Remind;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateRemindService {
	private final SaveRemindAdapter saveRemindAdapter;

	public void save(Plan plan, String message, TimeValue time) { // todo: plan 의존성
		Info info = new Info(message);
		Remind remind = Remind.plan(plan.getUserId(), plan.getId(), info, time.getMonth(), time.getDate());
		saveRemindAdapter.save(remind);
	}
}
