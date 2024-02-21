package me.ajaja.global.exception;

import me.ajaja.global.common.DiscordEvent;

public class UnexpectedExceptionEvent extends DiscordEvent {
	public UnexpectedExceptionEvent(String message) {
		super("@everyone " + message);
	}
}
