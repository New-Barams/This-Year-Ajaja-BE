package com.newbarams.ajaja.module.user.application.port.out;

import com.newbarams.ajaja.module.user.dto.UserResponse;

public interface GetMyPagePort {
	/**
	 * @param id target to query
	 * @return
	 */
	UserResponse.MyPage findUserInfoById(Long id); // todo: separate dto
}
