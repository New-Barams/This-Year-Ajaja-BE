package me.ajaja.module.remind.application.port.out;

import java.util.List;

import me.ajaja.global.common.TimeValue;
import me.ajaja.module.remind.domain.Remind;

public interface FindRemindablePlanPort {
	List<Remind> findAllRemindablePlan(String remindTime, String remindType, TimeValue time);
}
