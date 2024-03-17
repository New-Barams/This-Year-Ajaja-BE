package me.ajaja.module.remind.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.remind.application.port.out.FindTargetPort;
import me.ajaja.module.remind.application.port.out.FindTargetRemindQuery;
import me.ajaja.module.remind.dto.RemindResponse;
import me.ajaja.module.remind.mapper.RemindInfoMapper;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindTargetRemindAdapter implements FindTargetRemindQuery {
	private final RemindInfoMapper mapper;
	private final FindTargetPort findTargetPort;

	@Override
	public RemindResponse.RemindInfo findByUserIdAndPlanId(Long userId, Long planId) {
		Plan plan = findTargetPort.findByUserIdAndPlanId(userId, planId);

		List<RemindResponse.Message> messages = plan.getMessages()
			.stream().map(message ->
				mapper.toMessage(message, plan.getCreatedAt().getYear(), plan.getRemindTime()))
			.toList();

		return mapper.toRemindInfo(plan, messages);
	}
}
