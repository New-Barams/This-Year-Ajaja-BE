package com.newbarams.ajaja.module.user.application.service;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.security.jwt.util.JwtRemover;
import com.newbarams.ajaja.module.user.application.port.in.LogoutUseCase;
import com.newbarams.ajaja.module.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class LogoutService implements LogoutUseCase {
	private final UserRepository userRepository;
	private final JwtRemover jwtRemover;

	@Override
	public void logout(Long id) {
		validateExist(id);
		jwtRemover.remove(id);
	}

	private void validateExist(Long id) {
		if (!userRepository.existsById(id)) {
			throw new AjajaException(USER_NOT_FOUND);
		}
	}
}
