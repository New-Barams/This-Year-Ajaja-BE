package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawService {
	private final RetrieveUserService retrieveUserService;

	public void withdraw(Long userId) {
		User user = retrieveUserService.loadExistUserById(userId);
		user.delete(); // todo: add oauth separation, delete plans ... etc.
	}
}
