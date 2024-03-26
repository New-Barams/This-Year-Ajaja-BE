package me.ajaja.global.schedule;

import java.util.List;
import java.util.function.Supplier;

import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;

public interface Sendable {
	int MAX_TRY = 5;

	List<Integer> errors();

	String endPoint();

	Supplier<Integer> supply(); // todo: refactor

	default boolean isError(int status) {
		return errors().contains(status);
	}

	default void checkAttempts(int attempts) {
		if (attempts == MAX_TRY) {
			throw new AjajaException(ErrorCode.EXCEED_MAX_TRY);
		}
	}
}
