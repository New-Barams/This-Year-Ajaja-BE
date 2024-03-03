package me.ajaja.module.ajaja.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.schedule.EmailSendable;
import me.ajaja.infra.ses.SesSendAjajaRemindService;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.ajaja.mapper.AjajaMapper;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;

@Slf4j
@Component
class SendEmailAjajaStrategy extends SendAjajaStrategy implements EmailSendable {
	private final SesSendAjajaRemindService sesSendAjajaRemindService;
	private final AjajaMapper mapper;

	public SendEmailAjajaStrategy(
		AjajaQueryRepository ajajaQueryRepository,
		ApplicationEventPublisher eventPublisher,
		SaveAjajaRemindPort saveAjajaRemindPort,
		SesSendAjajaRemindService sesSendAjajaRemindService,
		AjajaMapper mapper
	) {
		super(ajajaQueryRepository, eventPublisher, saveAjajaRemindPort);
		this.sesSendAjajaRemindService = sesSendAjajaRemindService;
		this.mapper = mapper;
	}

	@Async
	@Override
	public void send() {
		loadRemindableAjajas(endPoint()).forEach(this::proceed);
	}

	@Override
	protected void proceed(Ajaja ajaja) {
		CompletableFuture
			.supplyAsync(emailSupplier(ajaja))
			.thenAccept(tries -> onSuccess(ajaja, createMessage(ajaja.getTitle(), ajaja.getCount()), endPoint(), tries))
			.exceptionally(super::handleFail);
	}

	@Override
	protected List<Ajaja> loadRemindableAjajas(String endPoint) {
		return ajajaQueryRepository.findRemindableAjajasByEndPoint(endPoint).stream()
			.map(mapper::toDomain)
			.toList();
	}

	private Supplier<Integer> emailSupplier(Ajaja ajaja) {
		return () -> {
			int attempts = 1;
			while (attempts <= MAX_TRY) {
				int statusCode = sesSendAjajaRemindService
					.send(ajaja.getEmail(), ajaja.getTitle(), ajaja.getCount(), ajaja.getTargetId());

				if (isError(statusCode)) {
					checkAttempts(attempts);
					continue;
				}
				break;
			}
			return attempts;
		};
	}
}
