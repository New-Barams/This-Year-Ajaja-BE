package me.ajaja.module.user.application;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.security.jwt.JwtRemover;
import me.ajaja.module.user.application.port.in.LogoutUseCase;
import me.ajaja.module.user.application.port.out.CheckExistUserPort;

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
