package me.ajaja.module.remind.application;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.context.ApplicationEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.BaseTime;
import me.ajaja.global.exception.UnexpectedExceptionEvent;
import me.ajaja.module.remind.application.port.out.FindRemindableTargetsPort;
import me.ajaja.module.remind.application.port.out.SaveRemindPort;
import me.ajaja.module.remind.domain.Remind;

@Slf4j
@RequiredArgsConstructor
abstract class SendRemindStrategy {
	private static final String FEEDBACK_URL_FORMAT =
		"https://www.ajaja.me/feedback/evaluate?title=%s&month=%d&day=%d&planId=%d";
	protected static final String MAIN_PAGE_URL = "https://www.ajaja.me/home";

	protected final FindRemindableTargetsPort findRemindableTargetsPort;
	private final ApplicationEventPublisher eventPublisher;
	private final SaveRemindPort saveRemindPort;

	public abstract void send(String remindTime, BaseTime now);

	public abstract void sendTrial(Remind remind);

	protected abstract void proceed(Remind remind, BaseTime now, Supplier<String> url);

	protected abstract List<Remind> loadRemindables(String scheduledTime, String endPoint, BaseTime now);

	protected void onSuccess(Remind unsaved, String sendType, BaseTime now, long tries) {
		log.info("[NCP] Remind Sent To : {} After {} tries", unsaved.getPhoneNumber(), tries);
		saveRemindPort.save(unsaved.asPlan(sendType, now));
	}

	protected Void handleFail(Throwable throwable) {
		eventPublisher.publishEvent(new UnexpectedExceptionEvent(throwable));
		return null;
	}

	protected static String toFeedbackUrl(String title, int month, int date, Long planId) {
		return FEEDBACK_URL_FORMAT.formatted(title, month, date, planId);
	}
}
