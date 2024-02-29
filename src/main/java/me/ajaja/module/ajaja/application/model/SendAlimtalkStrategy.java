package me.ajaja.module.ajaja.application.model;

import static me.ajaja.infra.feign.ncp.model.NaverRequest.Alimtalk.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.TimeValue;
import me.ajaja.infra.feign.ncp.client.NaverCloudProperties;
import me.ajaja.infra.feign.ncp.client.NaverSendAlimtalkFeignClient;
import me.ajaja.infra.feign.ncp.model.NaverRequest;
import me.ajaja.infra.feign.ncp.model.NaverResponse;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.ajaja.mapper.AjajaMapper;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;
import me.ajaja.module.remind.util.RemindExceptionHandler;

@Slf4j
@Component
public class SendAlimtalkStrategy extends SendAjajaStrategy {
	private static final List<Integer> errorCodes = List.of(400, 401, 403, 404, 500);
	private static final String endPoint = "KAKAO";

	private final NaverSendAlimtalkFeignClient naverSendAlimtalkFeignClient;
	private final NaverCloudProperties naverCloudProperties;
	private final AjajaMapper mapper;

	public SendAlimtalkStrategy(
		AjajaQueryRepository ajajaQueryRepository,
		RemindExceptionHandler exceptionHandler,
		SaveAjajaRemindPort saveAjajaRemindPort,
		NaverSendAlimtalkFeignClient naverSendAlimtalkFeignClient,
		NaverCloudProperties naverCloudProperties,
		AjajaMapper mapper
	) {
		super(ajajaQueryRepository, exceptionHandler, saveAjajaRemindPort);
		this.naverSendAlimtalkFeignClient = naverSendAlimtalkFeignClient;
		this.naverCloudProperties = naverCloudProperties;
		this.mapper = mapper;
	}

	@Override
	public void send(TimeValue now) {
		ajajaQueryRepository.findRemindableAjajasByEndPoint(endPoint).stream()
			.map(mapper::toDomain)
			.toList()
			.stream().filter(ajaja -> !Objects.equals(ajaja.getPhoneNumber(), "01000000000"))
			.forEach(ajaja -> {
				processResult(send(ajaja), ajaja, endPoint, now);
			});
	}

	@Async
	public CompletableFuture<String> send(Ajaja ajaja) {
		NaverRequest.Alimtalk request
			= ajaja(ajaja.getPhoneNumber(), ajaja.getCount(), ajaja.getTitle(), ajaja.getTargetId());

		return CompletableFuture.supplyAsync(alimtalkSupplier(request))
			.thenApply(tries -> {
				log.info("[NCP] Remind Sent To : {} After {} tries", ajaja.getPhoneNumber(), tries);
				return request.getMessages().get(0).getContent();
			});
	}

	private Supplier<Integer> alimtalkSupplier(NaverRequest.Alimtalk request) {
		return () -> {
			int attempts = 1;
			while (attempts <= ATTEMPTS_MAX_COUNT) {
				NaverResponse.AlimTalk response = naverSendAlimtalkFeignClient.send(naverCloudProperties.getServiceId(),
					request);

				int statusCode = Integer.parseInt(response.getStatusCode());
				if (isErrorOccurred(statusCode, errorCodes)) {
					attempts = checkAttemptsOrThrow(statusCode, attempts);
					continue;
				}
				break;
			}
			return attempts;
		};
	}
}
