package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.application.port.in.WithdrawUseCase;
import com.newbarams.ajaja.module.user.application.port.out.ApplyChangePort;
import com.newbarams.ajaja.module.user.application.port.out.DisablePlanPort;
import com.newbarams.ajaja.module.user.application.port.out.DisconnectOauthPort;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class WithdrawService implements WithdrawUseCase {
	private final RetrieveUserService retrieveUserService;
	private final DisconnectOauthPort disconnectOauthPort;
	private final DisablePlanPort disablePlanPort;
	private final ApplyChangePort applyChangePort;

	@Override
	public void withdraw(Long userId) {
		User user = retrieveUserService.loadExistById(userId);
		disconnectOauthPort.disconnect(user.getOauthId());
		disablePlanPort.disable(userId);
		user.delete();
		applyChangePort.apply(user);
	}
}
