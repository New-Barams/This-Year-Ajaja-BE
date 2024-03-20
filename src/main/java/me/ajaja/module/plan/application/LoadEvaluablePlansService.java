package me.ajaja.module.plan.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.BaseTime;
import me.ajaja.module.feedback.application.LoadEvaluableTargetsService;
import me.ajaja.module.feedback.application.model.UpdatableFeedback;
import me.ajaja.module.plan.application.port.out.FindEvaluablePlansPort;
import me.ajaja.module.plan.mapper.PlanMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadEvaluablePlansService implements LoadEvaluableTargetsService {
	private FindEvaluablePlansPort findEvaluablePlansPort;
	private PlanMapper mapper;

	@Override
	public List<UpdatableFeedback> findEvaluableTargetsByUserId(Long userId) {
		return findEvaluablePlansPort.findEvaluablePlansByUserId(userId).stream()
			.filter(plan -> plan.isWithinPeriod(BaseTime.now()))
			.map(mapper::toFeedbackModel)
			.toList();
	}
}
