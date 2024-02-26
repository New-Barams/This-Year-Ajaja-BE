package me.ajaja;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.DiscordEvent;
import me.ajaja.infra.discord.DiscordProperties;
import me.ajaja.infra.feign.kakao.client.KakaoProperties;
import me.ajaja.infra.feign.ncp.client.NaverCloudProperties;

@EnableAsync(proxyTargetClass = true)
@EnableCaching
@EnableScheduling
@EnableConfigurationProperties(value = {
	NaverCloudProperties.class,
	KakaoProperties.class,
	DiscordProperties.class
})
@SpringBootApplication
@RequiredArgsConstructor
public class AjajaApplication {
	private final ApplicationEventPublisher eventPublisher;
	private final Environment environment;

	@EventListener(ApplicationReadyEvent.class)
	private void started() {
		if (Arrays.asList(environment.getActiveProfiles()).contains("prod"))
			eventPublisher.publishEvent(new DiscordEvent("Production server started"));
	}

	public static void main(String[] args) {
		SpringApplication.run(AjajaApplication.class, args);
	}
}
