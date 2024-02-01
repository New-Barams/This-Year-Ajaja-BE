package com.newbarams.ajaja.module.remind.application.port.out;

import com.newbarams.ajaja.module.remind.domain.Remind;

public interface FindRemindAddressPort {
	Remind findAddressByUserId(Long userId);
}
