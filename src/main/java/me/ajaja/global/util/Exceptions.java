package me.ajaja.global.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Exceptions {

	public static String simplifyMessage(Throwable throwable) {
		Objects.requireNonNull(throwable, "throwable cannot be null!");
		StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
