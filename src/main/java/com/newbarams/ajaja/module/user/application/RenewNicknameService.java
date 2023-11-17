package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RenewNicknameService {
	private final RetrieveUserService retrieveUserService;

	public String renew(Long id) {
		User user = retrieveUserService.loadExistUserById(id);
		String nickname = RandomNicknameGenerator.generate();
		return user.updateNickname(nickname);
	}
}
