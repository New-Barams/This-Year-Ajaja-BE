package com.newbarams.ajaja.global.mock;

public sealed interface MockGetReminds permits MockGetReminds.Response {
	record Response(
		int index,
		String remindMessage
	) implements MockGetReminds {
	}
}
