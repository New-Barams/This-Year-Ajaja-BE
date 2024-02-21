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
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.ajaja.mapper.AjajaMapper;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;
import me.ajaja.module.remind.util.RemindExceptionHandler;

@Slf4j
@Component
public class SesSendAjajaRemindService extends SendAjajaStrategy {
	private static final List<Integer> HANDLING_ERROR_CODES = List.of(400, 408, 500, 503);
	private static final String END_POINT = "EMAIL";

	private final AmazonSimpleEmailService amazonSimpleEmailService;
	private final AjajaMapper mapper;

	public SesSendAjajaRemindService(
		AjajaQueryRepository ajajaQueryRepository,
		RemindExceptionHandler exceptionHandler,
		SaveAjajaRemindPort saveAjajaRemindPort,
		AmazonSimpleEmailService amazonSimpleEmailService,
		AjajaMapper mapper
	) {
		super(ajajaQueryRepository, exceptionHandler, saveAjajaRemindPort);
		this.amazonSimpleEmailService = amazonSimpleEmailService;
		this.mapper = mapper;
	}

	@Override
	public void send(TimeValue now) {
		List<Ajaja> ajajas = ajajaQueryRepository.findRemindableAjajasByEndPoint(END_POINT).stream()
			.map(mapper::toDomain)
			.toList();
		;
		ajajas.forEach(ajaja -> {
			send(ajaja).handle((message, exception) -> {
				if (exception != null) {
					exceptionHandler.handleRemindException(END_POINT, ajaja.getEmail(), exception.getMessage());
					return null;
				}
				saveAjajaRemindPort.save(ajaja.getUserId(), END_POINT, ajaja.getTargetId(), message, now);
				return null;
			});
		});
	}

	@Async
	public CompletableFuture<String> send(Ajaja ajaja) {
		MailForm mailForm = MailForm.ajaja(ajaja.getEmail(), ajaja.getTitle(), ajaja.getCount(), ajaja.getTargetId());
		return CompletableFuture.supplyAsync(emailSupplier(mailForm))
			.thenApply(tries -> {
				log.info("[SES] Ajaja Sent To : {} After {} tries", ajaja.getEmail(), tries);
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
