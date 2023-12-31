package com.newbarams.ajaja.module.user.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.user.application.port.out.RetrieveUserPort;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class RetrieveUserService {
	private final RetrieveUserPort retrieveUserPort;

	public User loadExistById(Long id) {
		return retrieveUserPort.load(id)
			.orElseThrow(() -> AjajaException.withId(id, USER_NOT_FOUND));
	}
}
