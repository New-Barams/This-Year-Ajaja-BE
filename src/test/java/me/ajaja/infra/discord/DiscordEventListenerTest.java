package me.ajaja.infra.discord;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import me.ajaja.global.event.DiscordEvent;

@SpringBootTest
@RecordApplicationEvents
class DiscordEventListenerTest {
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	@Autowired
	private ApplicationEvents events; // autowire success, just IDE error

	@MockBean
	private DiscordEventListener discordEventListener;

	@Test
	@DisplayName("디스코드 이벤트 발행되면 이벤트를 잘 받아야 한다.")
	void onDiscordEvent_Success() {
		// given

		// when
		eventPublisher.publishEvent(new DiscordEvent("test"));

		// then
		long count = events.stream(DiscordEvent.class).count();
		assertThat(count).isOne();

		then(discordEventListener).should(times(1)).onDiscordEvent(any());
	}
}
