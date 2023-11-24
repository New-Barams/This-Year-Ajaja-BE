package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawService {
	private final RetrieveUserService retrieveUserService;
	private final DisconnectService disconnectService;

	public void withdraw(Long userId, String authorizationCode, String redirectUri) {
		User user = retrieveUserService.loadExistUserById(userId);
		disconnectService.disconnect(authorizationCode, redirectUri);
		user.delete(); // todo: delete plans ... etc.
	}
}
