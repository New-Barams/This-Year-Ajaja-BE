package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.application.port.in.ChangeRemindTypeUseCase;
import com.newbarams.ajaja.module.user.application.port.out.ApplyChangePort;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class ChangeRemindTypeService implements ChangeRemindTypeUseCase {
	private final RetrieveUserService retrieveUserService;
	private final ApplyChangePort applyChangePort;

	@Override
	public void change(Long userId, User.RemindType remindType) {
		User user = retrieveUserService.loadExistById(userId);
		user.changeRemind(remindType);
		applyChangePort.apply(user);
	}
}
