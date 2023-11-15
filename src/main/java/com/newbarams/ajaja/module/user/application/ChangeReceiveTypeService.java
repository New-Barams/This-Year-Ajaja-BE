package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChangeReceiveTypeService {
	private final RetrieveUserService retrieveUserService;

	public void change(Long userId, String receiveType) {
		User user = retrieveUserService.loadExistUserById(userId);
		user.updateReceive(receiveType);
	}
}
