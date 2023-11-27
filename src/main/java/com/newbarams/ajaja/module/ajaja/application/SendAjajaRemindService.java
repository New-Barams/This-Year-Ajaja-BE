package com.newbarams.ajaja.module.ajaja.application;

import org.springframework.stereotype.Service;

@Service
public interface SendAjajaRemindService {
	void send(String email, Long ajajaNumber);
}
