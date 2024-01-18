package com.newbarams.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.remind.application.port.in.GetRemindInfoUseCase;
import com.newbarams.ajaja.module.remind.application.port.out.FindPlanRemindQuery;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetRemindInfoService implements GetRemindInfoUseCase {
	private final FindPlanRemindQuery findPlanRemindQuery;

	@Override
	public RemindResponse.RemindInfo load(Long userId, Long planId) {
		return findPlanRemindQuery.findByUserIdAndPlanId(userId, planId);
	}
}
