package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRemindEmailService {
	private final RetrieveUserService retrieveUserService;

	public void updateIfDifferent(Long id, String remindEmail) {
		User user = retrieveUserService.loadExistUserById(id);
		user.updateRemindEmailIfDifferent(remindEmail);
	}
}
