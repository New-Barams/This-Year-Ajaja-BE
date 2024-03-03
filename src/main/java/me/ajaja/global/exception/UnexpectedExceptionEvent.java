package me.ajaja.global.exception;

import me.ajaja.global.common.DiscordEvent;
import me.ajaja.global.util.Exceptions;

public class UnexpectedExceptionEvent extends DiscordEvent {

	public UnexpectedExceptionEvent(Throwable throwable) {
		super("@everyone " + Exceptions.simplifyMessage(throwable));
	}

	public UnexpectedExceptionEvent(RuntimeException exception) {
		super("@everyone " + Exceptions.simplifyMessage(exception));
	}
}
