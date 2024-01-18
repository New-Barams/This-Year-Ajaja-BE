package com.newbarams.ajaja.module.remind.application.port.out;

import com.newbarams.ajaja.module.remind.domain.Remind;

public interface SaveRemindPort {
	Remind save(Remind remind);
}
