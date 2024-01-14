package com.newbarams.ajaja.module.remind.application.port.out;

import java.util.List;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.remind.application.model.RemindMessageInfo;

public interface FindRemindablePlanPort {
	List<RemindMessageInfo> findAllRemindablePlan(String remindTime, TimeValue time);
}
