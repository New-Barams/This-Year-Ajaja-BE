package me.ajaja.module.user.application;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.user.application.port.out.RetrieveUserPort;
import me.ajaja.module.user.domain.User;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class RetrieveUserService {
	private final RetrieveUserPort retrieveUserPort;

	public User loadExistById(Long id) {
		return retrieveUserPort.loadById(id)
			.orElseThrow(() -> AjajaException.withId(id, USER_NOT_FOUND));
	}
}
