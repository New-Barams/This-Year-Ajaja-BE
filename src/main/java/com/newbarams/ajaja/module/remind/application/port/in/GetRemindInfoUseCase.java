package com.newbarams.ajaja.module.remind.application.port.in;

import com.newbarams.ajaja.module.remind.dto.RemindResponse;

public interface GetRemindInfoUseCase {
	RemindResponse.RemindInfo load(Long userId, Long planId);
}
