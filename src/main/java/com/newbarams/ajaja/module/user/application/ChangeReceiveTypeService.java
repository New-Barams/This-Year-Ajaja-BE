package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.application.port.in.ChangeReceiveTypeUseCase;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class ChangeReceiveTypeService implements ChangeReceiveTypeUseCase {
	private final RetrieveUserService retrieveUserService;
	private final UserRepository userRepository;

	@Override
	public void change(Long userId, User.ReceiveType receiveType) {
		User user = retrieveUserService.loadExistById(userId);
		user.updateReceive(receiveType);
		userRepository.save(user);
	}
}