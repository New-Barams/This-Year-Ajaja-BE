package me.ajaja.module.remind.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.BaseTime;
import me.ajaja.global.schedule.EmailSendable;
import me.ajaja.infra.ses.SesSendPlanRemindService;
import me.ajaja.module.remind.application.port.out.FindRemindableTargetsPort;
import me.ajaja.module.remind.domain.Remind;

@Slf4j
@Component
class SendEmailRemindStrategy extends SendRemindStrategy implements EmailSendable {
	private final SesSendPlanRemindService sesSendPlanRemindService;

	public SendEmailRemindStrategy(
		FindRemindableTargetsPort findRemindableTargetsPort,
		ApplicationEventPublisher eventPublisher,
		CreateRemindService createRemindService,
		SesSendPlanRemindService sesSendPlanRemindService
	) {
		super(findRemindableTargetsPort, eventPublisher, createRemindService);
		this.sesSendPlanRemindService = sesSendPlanRemindService;
	}

	@Async
	@Override
	public void send(String remindTime, BaseTime now) {
		loadRemindables(remindTime, endPoint(), now)
			.forEach(remind -> proceed(remind, now, () ->
				toFeedbackUrl(remind.getTitle(), now.getMonth(), now.getDate(), remind.getPlanId())));
	}

	@Async
	@Override
	public void sendTrial(Remind remind) {
		proceed(remind, BaseTime.now(), () -> MAIN_PAGE_URL);
	}

	@Override
	protected void proceed(Remind remind, BaseTime now, Supplier<String> url) {
		CompletableFuture
			.supplyAsync(emailSupplier(remind, url.get()))
			.thenAccept(tries -> super.onSuccess(remind, endPoint(), now, tries))
			.exceptionally(super::handleFail);
	}

	@Override
	protected List<Remind> loadRemindables(String scheduledTime, String endPoint, BaseTime now) {
		return findRemindableTargetsPort.findAllRemindablePlansByType(scheduledTime, endPoint, now);
	}

	private Supplier<Integer> emailSupplier(Remind remind, String feedbackUrl) {
		return () -> {
			int attempts = 1;
			while (attempts <= MAX_TRY) {
				int statusCode =
					sesSendPlanRemindService
						.send(remind.getEmail(), remind.getTitle(), remind.getMessage(), feedbackUrl);

				if (isError(statusCode)) {
					checkAttempts(attempts);
					attempts++;
					continue;
				}
				break;
			}
			return attempts;
		};
	}
}
