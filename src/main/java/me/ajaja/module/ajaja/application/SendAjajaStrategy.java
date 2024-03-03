package me.ajaja.module.ajaja.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.UnexpectedExceptionEvent;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;

@Slf4j
@RequiredArgsConstructor
abstract class SendAjajaStrategy {
	private static final String MESSAGE_FORMAT = "지난 주에 %s 계획을 %d명이나 응원했어요!";

	protected final AjajaQueryRepository ajajaQueryRepository;
	private final ApplicationEventPublisher eventPublisher;
	private final SaveAjajaRemindPort saveAjajaRemindPort;

	public abstract void send();

	protected abstract List<Ajaja> loadRemindableAjajas(String endPoint);

	protected abstract void proceed(Ajaja ajaja);

	protected void onSuccess(Ajaja ajaja, String message, String endPoint, long tries) {
		log.info("[NCP] Ajaja Sent To : {} After {} tries", ajaja.getPhoneNumber(), tries);
		saveAjajaRemindPort.save(ajaja.getUserId(), endPoint, ajaja.getTargetId(), message, TimeValue.now());
	}

	protected Void handleFail(Throwable throwable) {
		eventPublisher.publishEvent(new UnexpectedExceptionEvent(throwable));
		return null;
	}

	protected static String createMessage(String title, Long count) {
		return MESSAGE_FORMAT.formatted(title, count);
	}
}
