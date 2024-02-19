package me.ajaja.module.remind.application.port.out;

import me.ajaja.global.common.TimeValue;
import me.ajaja.module.remind.domain.Remind;

public interface SaveAjajaRemindPort {
	Remind save(Long userId, String endPoint, Long planId, String message, TimeValue now);
}
