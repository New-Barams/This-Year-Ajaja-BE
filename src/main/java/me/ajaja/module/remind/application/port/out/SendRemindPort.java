package me.ajaja.module.remind.application.port.out;

import me.ajaja.global.common.TimeValue;
import me.ajaja.module.remind.domain.Remind;

public interface SendRemindPort {
	String send(String remindTime, TimeValue now);

	String sendTest(Remind remind, String mainPageUrl);
}
