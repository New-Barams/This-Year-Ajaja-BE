package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;

@Service
public interface SendCertificationService {
	void send(String email, String certification);
}
