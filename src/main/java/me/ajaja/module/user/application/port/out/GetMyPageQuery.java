package me.ajaja.module.user.application.port.out;

import me.ajaja.module.user.dto.UserResponse;

public interface GetMyPageQuery {
	/**
	 * Inquiry user profile, Query port can be used on in coming adapter
	 * @param id target to inquiry
	 * @return User profile
	 */
	UserResponse.MyPage findUserInfoById(Long id);
}
