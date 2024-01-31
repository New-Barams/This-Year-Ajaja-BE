package com.newbarams.ajaja.module.remind.application.port.out;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.remind.domain.Remind;

public interface SendRemindPort {
	String send(String remindTime, TimeValue now);

	String sendTest(Remind remind, String mainPageUrl);
}
