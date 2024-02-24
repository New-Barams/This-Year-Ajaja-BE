package me.ajaja.module.remind.util;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RemindExceptionHandler {

	public void handleRemindException(String endPoint, String to, String errorMessage) {
		log.error("[{}] Remind Error Occurred To : {}, cause : {}", endPoint, to, errorMessage);
	}
}
