package com.newbarams.ajaja.module.user.application.port.in;

import com.newbarams.ajaja.module.user.domain.User;

public interface ChangeReceiveTypeUseCase {
	void change(Long userId, User.ReceiveType receiveType);
}
