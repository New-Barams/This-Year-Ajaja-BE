package me.ajaja.module.remind.util;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RemindExceptionHandler {
	public void handleRemindException(int errorCode, int tries) {
		log.warn("Send Alimtalk Remind Error Code : {} , retries : {}", errorCode, tries);
	}
}
