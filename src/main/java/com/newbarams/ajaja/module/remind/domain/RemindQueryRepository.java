package com.newbarams.ajaja.module.remind.domain;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

public interface RemindQueryRepository {
	RemindResponse.RemindInfo findAllReminds(Plan plan);
}
