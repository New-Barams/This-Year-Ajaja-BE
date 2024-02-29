package me.ajaja.global.schedule;

import java.util.List;
import java.util.function.Supplier;

public interface EmailSendable extends Sendable {

	@Override
	default List<Integer> getErrors() {
		return List.of(400, 408, 500, 503);
	}

	@Override
	default String getEndPoint() {
		return "EMAIL";
	}

	@Override
	default Supplier<Integer> supply() {
		return null;
	}
}
