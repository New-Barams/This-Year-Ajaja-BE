package me.ajaja.module.ajaja.application;

import org.springframework.stereotype.Service;

@Service
public interface SendAjajaRemindService {
	void send(String email, String title, Long ajajaNumber, Long planId);
}
