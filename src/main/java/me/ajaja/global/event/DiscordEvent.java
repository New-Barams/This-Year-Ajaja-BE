package me.ajaja.global.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DiscordEvent {
	private final String message;
}
