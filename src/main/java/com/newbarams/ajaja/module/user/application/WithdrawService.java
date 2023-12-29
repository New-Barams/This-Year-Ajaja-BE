package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.application.port.in.WithdrawUseCase;
import com.newbarams.ajaja.module.user.application.port.out.DeleteUserPort;
import com.newbarams.ajaja.module.user.application.port.out.DisablePlanPort;
import com.newbarams.ajaja.module.user.application.port.out.DisconnectOauthPort;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class WithdrawService implements WithdrawUseCase {
	private final DisconnectOauthPort disconnectOauthPort;
	private final DisablePlanPort disablePlanPort;
	private final DeleteUserPort deleteUserPort;

	@Override
	public void withdraw(Long userId, Long oauthId) {
		disconnectOauthPort.disconnect(oauthId);
		disablePlanPort.disable(userId);
		deleteUserPort.delete(userId);
	}
}
