package com.newbarams.ajaja.module.remind.application.port.out;

import com.newbarams.ajaja.module.remind.application.model.RemindAddress;

public interface FindRemindAddressPort {
	RemindAddress findAddressByUserId(Long userId);
}
