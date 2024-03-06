package me.ajaja.module.remind.application.port.out;

import java.util.List;

import me.ajaja.global.common.BaseTime;
import me.ajaja.module.remind.domain.Remind;

public interface FindRemindableTargetsPort {
	List<Remind> findAllRemindablePlansByType(String remindTime, String remindType, BaseTime time);
}
