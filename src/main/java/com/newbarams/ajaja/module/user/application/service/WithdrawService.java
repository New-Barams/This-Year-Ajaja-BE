package com.newbarams.ajaja.module.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.application.port.in.WithdrawUseCase;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class WithdrawService implements WithdrawUseCase {
	private final DisablePlanService disablePlanService;
	private final RetrieveUserService retrieveUserService;
	private final DisconnectOauthService disconnectOauthService;
	private final UserRepository userRepository;

	@Override
	public void withdraw(Long userId) {
		User user = retrieveUserService.loadExistUserById(userId);
		disconnectOauthService.disconnect(user.getOauthId());
		disablePlanService.disable(userId);
		user.delete();
		userRepository.save(user);
	}
}
