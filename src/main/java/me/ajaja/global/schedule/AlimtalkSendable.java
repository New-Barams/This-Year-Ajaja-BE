package me.ajaja.global.schedule;

import java.util.List;
import java.util.function.Supplier;

public interface AlimtalkSendable extends Sendable {

	@Override
	default List<Integer> errors() {
		return List.of(400, 401, 403, 404, 500);
	}

	@Override
	default String endPoint() {
		return "KAKAO";
	}

	@Override
	default Supplier<Integer> supply() {
		return null;
	}
}
