package me.ajaja.infra.ses;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;
import me.ajaja.module.remind.application.CreateRemindService;
import me.ajaja.module.remind.application.port.out.FindRemindableTargetPort;
import me.ajaja.module.remind.application.port.out.SendRemindPort;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.util.RemindExceptionHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class SesSendPlanRemindService implements SendRemindPort {
	private static final List<Integer> HANDLING_ERROR_CODES = List.of(400, 408, 500, 503);
	private static final String FEEDBACK_URL = """
		https://www.ajaja.me/feedback/evaluate?title=%s&month=%d&day=%d&planId=%d
		""";
	private static final String END_POINT = "EMAIL";
	private static final int RETRY_MAX_COUNT = 5;

	private final AmazonSimpleEmailService amazonSimpleEmailService;
	private final RemindExceptionHandler exceptionHandler;
	private final FindRemindableTargetPort findRemindableTargetPort;
	private final CreateRemindService createRemindService;

	@Override
	public String send(String remindTime, TimeValue now) {
		List<Remind> reminds = findRemindableTargetPort.findAllRemindablePlan(remindTime, END_POINT, now);
		reminds.forEach(remind -> {
			String url = FEEDBACK_URL.formatted(remind.getTitle(), now.getMonth(), now.getDate(), remind.getPlanId());
			send(remind, url)
				.exceptionally(e -> {
					log.warn("[SES] Remind Error Occur : {}", e.getMessage());
					throw new AjajaException(ErrorCode.EXTERNAL_API_FAIL);
				}).thenRun(() -> createRemindService.create(remind, now));
		});
		return END_POINT;
	}

	@Override
	public String sendTest(Remind remind, String mainPageUrl) {
		send(remind, mainPageUrl)
			.exceptionally(e -> {
				log.warn("[SES] Remind Error Occur : {}", e.getMessage());
				throw new AjajaException(ErrorCode.EXTERNAL_API_FAIL);
			});

		return END_POINT;
	}

	@Async
	public CompletableFuture<Void> send(Remind remind, String feedbackUrl) {
		MailForm mailForm = MailForm.remind(remind.getEmail(), remind.getTitle(), remind.getMessage(), feedbackUrl);
		return CompletableFuture.supplyAsync(emailSupplier(mailForm))
			.thenAccept(tries -> log.info("[SES] Remind Sent To : {} After {} tries", remind.getEmail(), tries));
	}

	private Supplier<Integer> emailSupplier(MailForm mailForm) {
		return () -> {
			int tries = 1;
			while (tries <= RETRY_MAX_COUNT) {
				SendEmailResult response = amazonSimpleEmailService.sendEmail(mailForm.toSesForm());

				if (isExceptionOccur(response.getSdkHttpMetadata().getHttpStatusCode(), tries)) {
					exceptionHandler.handleRemindException(response.getSdkHttpMetadata().getHttpStatusCode(), tries);
					tries++;
					continue;
				}
				break;
			}
			return tries;
		};
	}

	private boolean isExceptionOccur(int errorCode, int tries) {
		if (tries == RETRY_MAX_COUNT) {
			throw new AjajaException(ErrorCode.EXTERNAL_API_FAIL);
		}
		return HANDLING_ERROR_CODES.contains(errorCode);
	}
}
