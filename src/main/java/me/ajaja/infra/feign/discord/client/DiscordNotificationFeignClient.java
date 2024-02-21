package me.ajaja.infra.feign.discord.client;

import static me.ajaja.global.config.OpenFeignConfig.*;
import static org.springframework.http.MediaType.*;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "discord-send-notification", url = DEFINED_ON_RUNTIME)
public interface DiscordNotificationFeignClient {
	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	void send(URI webHookUri, @RequestBody String message);
}
