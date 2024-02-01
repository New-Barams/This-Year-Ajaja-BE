package me.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.remind.application.port.in.GetRemindInfoUseCase;
import me.ajaja.module.remind.application.port.out.FindPlanRemindQuery;
import me.ajaja.module.remind.dto.RemindResponse;

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
