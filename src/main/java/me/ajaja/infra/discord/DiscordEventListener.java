package me.ajaja.infra.discord;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.event.DiscordEvent;
import me.ajaja.infra.feign.discord.client.DiscordNotificationFeignClient;

@Component
@RequiredArgsConstructor
class DiscordEventListener {
	private final DiscordNotificationFeignClient discordNotificationFeignClient;
	private final DiscordProperties discordProperties;
	private final ObjectMapper mapper;

	@Async
	@EventListener
	public void onDiscordEvent(DiscordEvent discordEvent) {
		discordNotificationFeignClient.send(discordProperties.webHookUri(), toJson(discordEvent.getMessage()));
	}

	private String toJson(String rawMessage) {
		try {
			return mapper.writeValueAsString(Map.of("content", rawMessage));
		} catch (JsonProcessingException exception) {
			throw new RuntimeException(exception);
		}
	}
}
