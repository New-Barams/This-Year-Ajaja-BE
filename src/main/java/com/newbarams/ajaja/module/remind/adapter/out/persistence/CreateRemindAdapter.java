package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.application.port.in.CreateRemindUseCase;
import com.newbarams.ajaja.module.remind.domain.Info;
import com.newbarams.ajaja.module.remind.domain.Remind;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CreateRemindAdapter implements CreateRemindUseCase {
	private final SaveRemindAdapter saveRemindAdapter;

	public void save(Plan plan, String message, TimeValue time) { // todo: plan 의존성
		Info info = new Info(message);
		Remind remind = Remind.plan(plan.getUserId(), plan.getId(), info, time.getMonth(), time.getDate());
		saveRemindAdapter.save(remind);
	}
}
