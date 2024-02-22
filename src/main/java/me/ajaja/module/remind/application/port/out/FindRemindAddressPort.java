package me.ajaja.module.remind.application.port.out;

import me.ajaja.module.remind.domain.Remind;

public interface FindRemindAddressPort {
	Remind findAddressByUserId(Long userId);
}
