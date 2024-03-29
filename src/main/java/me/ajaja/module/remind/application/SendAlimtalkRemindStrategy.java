package me.ajaja.module.remind.application;

import static me.ajaja.infra.feign.ncp.model.NaverRequest.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.BaseTime;
import me.ajaja.global.schedule.AlimtalkSendable;
import me.ajaja.infra.feign.ncp.model.NaverResponse;
import me.ajaja.module.remind.application.port.out.FindRemindableTargetsPort;
import me.ajaja.module.remind.application.port.out.SaveRemindPort;
import me.ajaja.module.remind.application.port.out.SendRemindPort;
import me.ajaja.module.remind.domain.Remind;

@Slf4j
@Component
class SendAlimtalkRemindStrategy extends SendRemindStrategy implements AlimtalkSendable {
	private final SendRemindPort sendRemindPort;

	public SendAlimtalkRemindStrategy(
		FindRemindableTargetsPort findRemindableTargetsPort,
		ApplicationEventPublisher eventPublisher,
		SaveRemindPort saveRemindPort,
		SendRemindPort sendRemindPort
	) {
		super(findRemindableTargetsPort, eventPublisher, saveRemindPort);
		this.sendRemindPort = sendRemindPort;
	}

	@Async
	@Override
	public void send(String remindTime, BaseTime now) {
		loadRemindables(remindTime, endPoint(), now).stream()
			.filter(Remind::isValidNumber)
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
		Alimtalk request = Alimtalk.remind(remind.getPhoneNumber(), remind.getTitle(), remind.getMessage(), url.get());

		CompletableFuture
			.supplyAsync(alimtalkSupplier(request))
			.thenAccept(tries -> super.onSuccess(remind, endPoint(), now, tries))
			.exceptionally(super::handleFail);
	}

	@Override
	protected List<Remind> loadRemindables(String scheduledTime, String endPoint, BaseTime now) {
		return findRemindableTargetsPort.findAllRemindablePlansByType(scheduledTime, endPoint, now);
	}

	private Supplier<Integer> alimtalkSupplier(Alimtalk request) {
		return () -> {
			int attempts = 1;
			while (attempts <= MAX_TRY) {
				NaverResponse.AlimTalk response = sendRemindPort.send(request);

				int statusCode = Integer.parseInt(response.getStatusCode());
				if (isError(statusCode)) {
					checkAttempts(attempts);
					// todo: log
					attempts++;
					continue;
				}
				break;
			}
			return attempts;
		};
	}
}
