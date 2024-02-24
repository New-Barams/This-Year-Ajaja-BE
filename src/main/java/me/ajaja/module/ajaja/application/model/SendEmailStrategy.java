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
	private static final List<Integer> HANDLING_ERROR_CODES = List.of(400, 408, 500, 503);
	private static final String END_POINT = "EMAIL";

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
		List<Ajaja> ajajas = ajajaQueryRepository.findRemindableAjajasByEndPoint(END_POINT).stream()
			.map(mapper::toDomain)
			.toList();

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
		return CompletableFuture.supplyAsync(emailSupplier(ajaja))
			.thenApply(tries -> {
				log.info("[SES] Ajaja Sent To : {} After {} tries", ajaja.getEmail(), tries);
				return createAjajaMessage(ajaja.getTitle(), ajaja.getCount());
			});
	}

	private String createAjajaMessage(String title, Long count) {
		return "지난 주에 " + title + " 계획 계획을 " + count + "명이나 응원했어요";
	}

	private Supplier<Integer> emailSupplier(Ajaja ajaja) {
		return () -> {
			int tries = 1;
			while (tries <= RETRY_MAX_COUNT) {
				int statusCode = sesSendAjajaRemindService.send(ajaja.getEmail(), ajaja.getTitle(), ajaja.getCount(),
					ajaja.getTargetId());

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
