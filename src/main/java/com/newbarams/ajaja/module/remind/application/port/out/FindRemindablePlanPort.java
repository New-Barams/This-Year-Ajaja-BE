package com.newbarams.ajaja.module.remind.application.port.out;

import java.util.List;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.remind.domain.Remind;

public interface FindRemindablePlanPort {
	List<Remind> findAllRemindablePlan(String remindTime, TimeValue time);
}
