package me.ajaja.module.remind.adapter.out.infra;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;
import me.ajaja.infra.ses.SesSendPlanRemindService;
import me.ajaja.module.remind.application.CreateRemindService;
import me.ajaja.module.remind.application.port.out.FindRemindableTargetsPort;
import me.ajaja.module.remind.application.port.out.SendRemindPort;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.util.RemindExceptionHandler;

@Slf4j
@Component
public class SendEmailAdapter extends SendRemindPort {
	private static final List<Integer> HANDLING_ERROR_CODES = List.of(400, 408, 500, 503);
	private static final String END_POINT = "EMAIL";

	private final SesSendPlanRemindService sesSendPlanRemindService;

	public SendEmailAdapter(
		FindRemindableTargetsPort findRemindableTargetsPort,
		RemindExceptionHandler exceptionHandler,
		CreateRemindService createRemindService,
		SesSendPlanRemindService sesSendPlanRemindService
	) {
		super(findRemindableTargetsPort, exceptionHandler, createRemindService);
		this.sesSendPlanRemindService = sesSendPlanRemindService;
	}

	@Override
	public void send(String remindTime, TimeValue now) {
		List<Remind> reminds = findRemindableTargetsPort.findAllRemindablePlansByType(remindTime, END_POINT, now);
		reminds.forEach(remind -> {
			String url = this.toFeedbackUrl(remind.getTitle(), now.getMonth(), now.getDate(), remind.getPlanId());
			send(remind, url).handle((message, exception) -> {
				if (exception != null) {
					exceptionHandler.handleRemindException(END_POINT, remind.getEmail(), exception.getMessage());
					return null;
				}
				createRemindService.create(remind, now, END_POINT);
				return null;
			});
		});
	}

	@Override
	public void sendTrial(Remind remind, String mainPageUrl) {
		send(remind, mainPageUrl)
			.exceptionally(e -> {
				log.warn("[SES] Remind Error Occur : {}", e.getMessage());
				throw new AjajaException(ErrorCode.EXTERNAL_API_FAIL);
			});
	}

	@Async
	public CompletableFuture<String> send(Remind remind, String feedbackUrl) {
		return CompletableFuture.supplyAsync(emailSupplier(remind, feedbackUrl))
			.thenApply(tries -> {
				log.info("[SES] Remind Sent To : {} After {} tries", remind.getEmail(), tries);
				return remind.getMessage();
			});
	}

	private Supplier<Integer> emailSupplier(Remind remind, String feedbackUrl) {
		return () -> {
			int tries = 1;
			while (tries <= RETRY_MAX_COUNT) {
				int statusCode =
					sesSendPlanRemindService
						.send(remind.getEmail(), remind.getTitle(), remind.getMessage(), feedbackUrl);

				if (isErrorOccurred(statusCode)) {
					validateTryCount(tries);
					log.warn("Send SES Remind Error Code : {} , retries : {}",
						statusCode, tries);
					tries++;
					continue;
				}
				break;
			}
			return tries;
		};
	}

	private boolean isErrorOccurred(int statusCode) {
		return HANDLING_ERROR_CODES.contains(statusCode);
	}
}
