package me.ajaja.module.ajaja.application;

import static me.ajaja.infra.feign.ncp.model.NaverRequest.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.schedule.AlimtalkSendable;
import me.ajaja.infra.feign.ncp.model.NaverResponse;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.ajaja.mapper.AjajaMapper;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;

@Slf4j
@Component
class SendAlimtalkAjajaStrategy extends SendAjajaStrategy implements AlimtalkSendable {
	private final SendAjajaRemindPort sendAjajaRemindPort;
	private final AjajaMapper mapper;

	public SendAlimtalkAjajaStrategy(
		AjajaQueryRepository ajajaQueryRepository,
		ApplicationEventPublisher eventPublisher,
		SaveAjajaRemindPort saveAjajaRemindPort,
		SendAjajaRemindPort sendAjajaRemindPort,
		AjajaMapper mapper
	) {
		super(ajajaQueryRepository, eventPublisher, saveAjajaRemindPort);
		this.sendAjajaRemindPort = sendAjajaRemindPort;
		this.mapper = mapper;
	}

	@Async
	@Override
	public void send() {
		loadRemindableAjajas(endPoint()).stream()
			.filter(Ajaja::isValidNumber)
			.forEach(this::proceed);
	}

	@Override
	protected void proceed(Ajaja ajaja) {
		Alimtalk request =
			Alimtalk.ajaja(ajaja.getPhoneNumber(), ajaja.getCount(), ajaja.getTitle(), ajaja.getTargetId());

		CompletableFuture
			.supplyAsync(alimtalkSupplier(request))
			.thenAccept(tries -> super.onSuccess(ajaja, request.getMessages().get(0).getContent(), endPoint(), tries))
			.exceptionally(super::handleFail);
	}

	@Override
	protected List<Ajaja> loadRemindableAjajas(String endPoint) {
		return ajajaQueryRepository.findRemindableAjajasByEndPoint(endPoint).stream()
			.map(mapper::toDomain)
			.toList();
	}

	private Supplier<Integer> alimtalkSupplier(Alimtalk request) {
		return () -> {
			int attempts = 1;
			while (attempts <= MAX_TRY) {
				NaverResponse.AlimTalk response = sendAjajaRemindPort.send(request);

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
