package com.newbarams.ajaja.module.user.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.security.jwt.JwtRemover;
import com.newbarams.ajaja.module.user.application.port.in.LogoutUseCase;
import com.newbarams.ajaja.module.user.application.port.out.CheckExistUserPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class LogoutService implements LogoutUseCase {
	private final CheckExistUserPort checkExistUserPort;
	private final JwtRemover jwtRemover;

	@Override
	public void logout(Long id) {
		validateExist(id);
		jwtRemover.remove(id);
	}

	private void validateExist(Long id) {
		if (!checkExistUserPort.isExist(id)) {
			throw new AjajaException(USER_NOT_FOUND);
		}
	}
}
