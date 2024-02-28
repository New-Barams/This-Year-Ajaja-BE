package me.ajaja.module.ajaja.application.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;
import me.ajaja.module.remind.application.port.out.Sendable;
import me.ajaja.module.remind.util.RemindExceptionHandler;

@Slf4j
@RequiredArgsConstructor
public abstract class SendAjajaStrategy implements Sendable {
	protected final AjajaQueryRepository ajajaQueryRepository;
	private final RemindExceptionHandler exceptionHandler;
	private final SaveAjajaRemindPort saveAjajaRemindPort;

	public abstract void send(TimeValue now);

	protected void processResult(CompletableFuture<String> result, Ajaja ajaja, String endPoint, TimeValue now) {
		result.handle((message, exception) -> {
			if (exception != null) {
				exceptionHandler.handleRemindException(endPoint, ajaja.getPhoneNumber(), exception.getMessage());
				return null;
			}
			saveAjajaRemindPort.save(ajaja.getUserId(), endPoint, ajaja.getTargetId(), message, now);
			return null;
		});
	}

	protected String createAjajaMessage(String title, Long count) {
		return "지난 주에 " + title + " 계획 계획을 " + count + "명이나 응원했어요";
	}

	@Override
	public boolean isErrorOccurred(int responseCode, List<Integer> handlingErrors) {
		return handlingErrors.contains(responseCode);
	}

	@Override
	public int checkAttemptsOrThrow(int statusCode, int attempts) {
		if (attempts == ATTEMPTS_MAX_COUNT) {
			throw new AjajaException(ErrorCode.ATTEMPTS_EXCEED_MAXIMUM_COUNT);
		}
		log.warn("Send Remind Error Code : {} , retries : {}", statusCode, attempts);
		return ++attempts;
	}
}
