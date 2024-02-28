package me.ajaja.module.plan.application.port.out;

import java.util.List;

import me.ajaja.global.common.TimeValue;
import me.ajaja.module.remind.application.model.RemindMessageInfo;

public interface FindRemindablePlanPort {
	List<RemindMessageInfo> findAllPlansByTime(String remindTime, String remindType, TimeValue time);
}
