package me.ajaja.module.ajaja.application.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.TimeValue;
import me.ajaja.infra.ses.SesSendAjajaRemindService;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.ajaja.mapper.AjajaMapper;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;
import me.ajaja.module.remind.util.RemindExceptionHandler;

@Slf4j
@Component
public class SendEmailStrategy extends SendAjajaStrategy {
	private final List<Integer> errorCodes = List.of(400, 408, 500, 503);
	private final String endPoint = "EMAIL";

	private final SesSendAjajaRemindService sesSendAjajaRemindService;
	private final AjajaMapper mapper;

	public SendEmailStrategy(
		AjajaQueryRepository ajajaQueryRepository,
		RemindExceptionHandler exceptionHandler,
		SaveAjajaRemindPort saveAjajaRemindPort,
		SesSendAjajaRemindService sesSendAjajaRemindService,
		AjajaMapper mapper
	) {
		super(ajajaQueryRepository, exceptionHandler, saveAjajaRemindPort);
		this.sesSendAjajaRemindService = sesSendAjajaRemindService;
		this.mapper = mapper;
	}

	@Override
	public void send(TimeValue now) {
		ajajaQueryRepository.findRemindableAjajasByEndPoint(endPoint).stream()
			.map(mapper::toDomain)
			.toList()
			.forEach(ajaja -> processResult(send(ajaja), ajaja, endPoint, now));
	}

	@Async
	public CompletableFuture<String> send(Ajaja ajaja) {
		return CompletableFuture.supplyAsync(emailSupplier(ajaja))
			.thenApply(tries -> {
				log.info("[SES] Ajaja Sent To : {} After {} tries", ajaja.getEmail(), tries);
				return createAjajaMessage(ajaja.getTitle(), ajaja.getCount());
			});
	}

	private Supplier<Integer> emailSupplier(Ajaja ajaja) {
		return () -> {
			int attempts = 1;
			while (attempts <= ATTEMPTS_MAX_COUNT) {
				int statusCode = sesSendAjajaRemindService.send(ajaja.getEmail(), ajaja.getTitle(), ajaja.getCount(),
					ajaja.getTargetId());

				if (isErrorOccurred(statusCode)) {
					attempts = checkAttemptsOrThrow(statusCode, attempts);
					continue;
				}
				break;
			}
			return attempts;
		};
	}

	private boolean isErrorOccurred(int statusCode) {
		return errorCodes.contains(statusCode);
	}
}
