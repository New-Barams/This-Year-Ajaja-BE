package me.ajaja.module.remind.application.port.out;

import me.ajaja.module.plan.domain.Plan;

public interface SaveTargetPort {
	void update(Plan plan);
}
