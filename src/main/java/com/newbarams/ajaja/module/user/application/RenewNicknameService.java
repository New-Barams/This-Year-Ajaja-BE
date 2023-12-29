package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.application.port.in.RenewNicknameUseCase;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class RenewNicknameService implements RenewNicknameUseCase {
	private final RetrieveUserService retrieveUserService;
	private final UserRepository userRepository;

	@Override
	public String renew(Long id) {
		User user = retrieveUserService.loadExistById(id);
		String newNickname = user.updateNickname();
		userRepository.save(user);
		return newNickname;
	}
}