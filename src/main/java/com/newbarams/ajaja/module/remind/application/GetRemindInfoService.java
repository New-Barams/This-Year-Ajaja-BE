package com.newbarams.ajaja.module.remind.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.application.port.in.GetRemindInfoUseCase;
import com.newbarams.ajaja.module.remind.application.port.out.FindPlanPort;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;
import com.newbarams.ajaja.module.remind.mapper.RemindInfoMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetRemindInfoService implements GetRemindInfoUseCase {
	private final FindPlanPort findPlanPort;
	private final RemindInfoMapper mapper;

	@Override
	public RemindResponse.RemindInfo load(Long userId, Long planId) {
		Plan plan = findPlanPort.findByUserIdAndPlanId(userId, planId);
		List<RemindResponse.Message> messages = plan.getMessages().stream().map(mapper::toMessage).toList();

		return mapper.toRemindInfo(plan, messages);
	}
}
