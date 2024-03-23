package me.ajaja.module.plan.application.port.out;

import java.util.List;

import me.ajaja.global.common.BaseTime;
import me.ajaja.module.plan.domain.Plan;

public interface FindEvaluablePlansPort {
	List<Plan> findEvaluablePlansByUserIdAndTime(Long id, BaseTime now);
}
