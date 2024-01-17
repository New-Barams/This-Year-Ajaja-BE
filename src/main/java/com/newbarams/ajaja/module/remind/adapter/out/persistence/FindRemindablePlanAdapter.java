package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.remind.application.port.out.FindRemindablePlanPort;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.mapper.RemindMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FindRemindablePlanAdapter implements FindRemindablePlanPort {
	private final PlanQueryRepository planQueryRepository;
	private final RemindMapper mapper;

	@Override
	public List<Remind> findAllRemindablePlan(String remindTime, TimeValue time) {
		return planQueryRepository.findAllRemindablePlan(remindTime, time).stream()
			.map(info -> mapper.toDomain(info, time))
			.toList();
	}
}
