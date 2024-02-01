package me.ajaja.module.user.application.port.out;

import me.ajaja.module.remind.application.model.RemindAddress;

public interface FindUserAddressPort {
	RemindAddress findUserAddressByUserId(Long userId);
}
