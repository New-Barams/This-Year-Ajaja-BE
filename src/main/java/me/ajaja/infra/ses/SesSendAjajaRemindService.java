package me.ajaja.infra.ses;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.ajaja.application.model.SendAjajaStrategy;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.remind.application.model.RemindableAjaja;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;
import me.ajaja.module.remind.util.RemindExceptionHandler;

@Slf4j
@Component
public class SesSendAjajaRemindService extends SendAjajaStrategy {
	private static final List<Integer> HANDLING_ERROR_CODES = List.of(400, 408, 500, 503);
	private static final String END_POINT = "EMAIL";

	private final AmazonSimpleEmailService amazonSimpleEmailService;

	public SesSendAjajaRemindService(
		AjajaQueryRepository ajajaQueryRepository,
		RemindExceptionHandler exceptionHandler,
		SaveAjajaRemindPort saveAjajaRemindPort,
		AmazonSimpleEmailService amazonSimpleEmailService
	) {
		super(ajajaQueryRepository, exceptionHandler, saveAjajaRemindPort);
		this.amazonSimpleEmailService = amazonSimpleEmailService;
	}

	@Override
	public void send(TimeValue now) {
		List<RemindableAjaja> remindableAjajas = ajajaQueryRepository.findRemindableAjajasByEndPoint(END_POINT);
		remindableAjajas.forEach(ajaja -> {
			send(ajaja).handle((message, exception) -> {
				if (exception != null) {
					exceptionHandler.handleRemindException(END_POINT, ajaja.email(), exception.getMessage());
					return null;
				}
				saveAjajaRemindPort.save(ajaja.userId(), ajaja.planId(), message, now);
				return null;
			});
		});
	}

	@Async
	public CompletableFuture<String> send(RemindableAjaja ajaja) {
		MailForm mailForm = MailForm.ajaja(ajaja.email(), ajaja.title(), ajaja.count(), ajaja.planId());
		return CompletableFuture.supplyAsync(emailSupplier(mailForm))
			.thenApply(tries -> {
				log.info("[SES] Ajaja Sent To : {} After {} tries", ajaja.email(), tries);
				return mailForm.toSesForm().getMessage().getSubject().getData();
			});
	}

	private Supplier<Integer> emailSupplier(MailForm mailForm) {
		return () -> {
			int tries = 1;
			while (tries <= RETRY_MAX_COUNT) {
				SendEmailResult response = amazonSimpleEmailService.sendEmail(mailForm.toSesForm());

				if (isErrorOccurred(response.getSdkHttpMetadata().getHttpStatusCode())) {
					validateTryCount(tries);
					log.warn("Send SES Remind Error Code : {} , retries : {}",
						response.getSdkHttpMetadata().getHttpStatusCode(), tries);
					tries++;
					continue;
				}
				break;
			}
			return tries;
		};
	}

	private boolean isErrorOccurred(int errorCode) {
		return HANDLING_ERROR_CODES.contains(errorCode);
	}
}
