package com.newbarams.ajaja.module.user.application.port.in;

import com.newbarams.ajaja.module.user.domain.User;

public interface ChangeRemindTypeUseCase {
	/**
	 * Change remind receive type use case
	 * @see User.ReceiveType
	 * @param userId target to change
	 * @param receiveType support three type : KAKAO, EMAIL, BOTH
	 */
	void change(Long userId, User.ReceiveType receiveType);
}
