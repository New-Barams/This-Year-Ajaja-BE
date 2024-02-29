package me.ajaja.module.remind.application.port.out;

import java.util.List;

public interface Sendable {
	int ATTEMPTS_MAX_COUNT = 5;

	// void send(); // todo: need implement
	boolean isErrorOccurred(int responseCode, List<Integer> handlingErrors);

	int checkAttemptsOrThrow(int statusCode, int attempts);
}
