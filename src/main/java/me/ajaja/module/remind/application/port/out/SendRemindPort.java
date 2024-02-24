package me.ajaja.module.remind.application.port.out;

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
public abstract class SendRemindPort {
	private static final String FEEDBACK_URL = """
		https://www.ajaja.me/feedback/evaluate?title=%s&month=%d&day=%d&planId=%d
		""";
	protected static final int RETRY_MAX_COUNT = 5;

	protected final FindRemindableTargetsPort findRemindableTargetsPort;
	protected final RemindExceptionHandler exceptionHandler;
	protected final CreateRemindService createRemindService;

	public abstract void send(String remindTime, TimeValue now);

	public abstract void sendTrial(Remind remind, String mainPageUrl);

	protected void validateTryCount(int tries) {
		if (tries == RETRY_MAX_COUNT) {
			throw new AjajaException(ErrorCode.ATTEMPTS_EXCEED_MAXIMUM_COUNT);
		}
	}

	protected String toFeedbackUrl(String title, int month, int date, Long planId) {
		return FEEDBACK_URL.formatted(title, month, date, planId);
	}
}
