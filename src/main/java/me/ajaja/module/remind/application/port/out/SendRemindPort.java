package me.ajaja.module.remind.application.port.out;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;
import me.ajaja.module.remind.application.CreateRemindService;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.util.RemindExceptionHandler;

@Slf4j
@RequiredArgsConstructor
public abstract class SendRemindPort implements Sendable {
	private static final String FEEDBACK_URL = """
		https://www.ajaja.me/feedback/evaluate?title=%s&month=%d&day=%d&planId=%d
		""";

	protected final FindRemindableTargetsPort findRemindableTargetsPort;
	private final RemindExceptionHandler exceptionHandler;
	private final CreateRemindService createRemindService;

	public abstract void send(String remindTime, TimeValue now);

	public abstract void sendTrial(Remind remind, String mainPageUrl);

	protected void processResult(CompletableFuture<String> result, Remind remind, String endPoint, TimeValue now) {
		result.handle((message, exception) -> {
			if (exception != null) {
				exceptionHandler.handleRemindException(endPoint, remind.getEmail(), exception.getMessage());
				return null;
			}
			createRemindService.create(remind, now, endPoint);
			return null;
		});
	}

	protected String toFeedbackUrl(String title, int month, int date, Long planId) {
		return FEEDBACK_URL.formatted(title, month, date, planId);
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
