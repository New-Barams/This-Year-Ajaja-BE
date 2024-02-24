package me.ajaja.module.ajaja.application.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;
import me.ajaja.module.remind.util.RemindExceptionHandler;

@Slf4j
@RequiredArgsConstructor
public abstract class SendAjajaStrategy {
	protected static final int RETRY_MAX_COUNT = 5;

	protected final AjajaQueryRepository ajajaQueryRepository;
	protected final RemindExceptionHandler exceptionHandler;
	protected final SaveAjajaRemindPort saveAjajaRemindPort;

	public abstract void send(TimeValue now);

	protected void validateTryCount(int tries) {
		if (tries == RETRY_MAX_COUNT) {
			throw new AjajaException(ErrorCode.ATTEMPTS_EXCEED_MAXIMUM_COUNT);
		}
	}
}
