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
	private static final List<String> HANDLING_ERROR_CODES = List.of("400", "401", "403", "404", "500");
	private static final String END_POINT = "KAKAO";

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
		List<Ajaja> ajajas = ajajaQueryRepository.findRemindableAjajasByEndPoint(END_POINT).stream()
			.map(mapper::toDomain)
			.toList();

		ajajas.stream().filter(ajaja -> !Objects.equals(ajaja.getPhoneNumber(), "01000000000"))
			.forEach(ajaja -> {
				send(ajaja).handle((message, exception) -> {
					if (exception != null) {
						exceptionHandler.handleRemindException(END_POINT, ajaja.getPhoneNumber(),
							exception.getMessage());
						return null;
					}
					saveAjajaRemindPort.save(ajaja.getUserId(), END_POINT, ajaja.getTargetId(), message, now);
					return null;
				});
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
			int tries = 1;
			while (tries <= RETRY_MAX_COUNT) {
				NaverResponse.AlimTalk response = naverSendAlimtalkFeignClient.send(naverCloudProperties.getServiceId(),
					request);
				if (isErrorOccurred(response.getStatusCode())) {
					validateTryCount(tries);
					log.warn("Send Alimtalk Remind Error Code : {} , retries : {}", response.getStatusCode(), tries);
					tries++;
					continue;
				}
				break;
			}
			return tries;
		};
	}

	private boolean isErrorOccurred(String statusCode) {
		return HANDLING_ERROR_CODES.contains(statusCode);
	}
}
