package me.ajaja.module.remind.application.port.in;

import me.ajaja.module.remind.dto.RemindResponse;

public interface GetRemindInfoUseCase {
	RemindResponse.RemindInfo load(Long userId, Long planId);
}
