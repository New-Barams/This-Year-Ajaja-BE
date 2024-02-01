package me.ajaja.module.user.application.port.in;

import me.ajaja.module.user.domain.User;

public interface ChangeRemindTypeUseCase {
	/**
	 * Change remind receive type use case
	 * @see User.RemindType
	 * @param userId target to change
	 * @param remindType support three type : KAKAO, EMAIL, BOTH
	 */
	void change(Long userId, User.RemindType remindType);
}
