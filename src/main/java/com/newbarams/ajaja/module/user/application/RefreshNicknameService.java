package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.application.port.in.RefreshNicknameUseCase;
import com.newbarams.ajaja.module.user.application.port.out.ApplyChangePort;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class RefreshNicknameService implements RefreshNicknameUseCase {
	private final RetrieveUserService retrieveUserService;
	private final ApplyChangePort applyChangePort;

	@Override
	public void refresh(Long id) {
		User user = retrieveUserService.loadExistById(id);
		user.refreshNickname();
		applyChangePort.apply(user);
	}
}
