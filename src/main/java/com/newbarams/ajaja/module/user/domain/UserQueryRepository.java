package com.newbarams.ajaja.module.user.domain;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.user.dto.UserResponse;

@Repository
public interface UserQueryRepository {
	UserResponse.MyPage findUserInfoById(Long userId);
}
