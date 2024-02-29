package me.ajaja.module.remind.adapter.out.infra;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;
import me.ajaja.infra.feign.ncp.client.NaverCloudProperties;
import me.ajaja.infra.feign.ncp.client.NaverSendAlimtalkFeignClient;
import me.ajaja.infra.feign.ncp.model.NaverRequest;
import me.ajaja.infra.feign.ncp.model.NaverResponse;
import me.ajaja.module.remind.application.CreateRemindService;
import me.ajaja.module.remind.application.port.out.FindRemindableTargetsPort;
import me.ajaja.module.remind.application.port.out.SendRemindPort;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.util.RemindExceptionHandler;

@Slf4j
@Component
public class SendAlimtalkAdapter extends SendRemindPort {
	private final List<Integer> errorCodes = List.of(400, 401, 403, 404, 500);
	private final String endPoint = "KAKAO";

	private final NaverSendAlimtalkFeignClient naverSendAlimtalkFeignClient;
	private final NaverCloudProperties naverCloudProperties;

	public SendAlimtalkAdapter(
		FindRemindableTargetsPort findRemindableTargetsPort,
		RemindExceptionHandler exceptionHandler,
		CreateRemindService createRemindService,
		NaverSendAlimtalkFeignClient naverSendAlimtalkFeignClient,
		NaverCloudProperties naverCloudProperties
	) {
		super(findRemindableTargetsPort, exceptionHandler, createRemindService);
		this.naverSendAlimtalkFeignClient = naverSendAlimtalkFeignClient;
		this.naverCloudProperties = naverCloudProperties;
	}

	@Override
	public void send(String remindTime, TimeValue now) {
		findRemindableTargetsPort.findAllRemindablePlansByType(remindTime, endPoint, now).stream()
			.filter(remind -> !Objects.equals(remind.getPhoneNumber(), "01000000000"))
			.forEach(remind -> {
				String url = this.toFeedbackUrl(remind.getTitle(), now.getMonth(), now.getDate(), remind.getPlanId());
				processResult(sendAlimtalk(remind, url), remind, endPoint, now);
			});
	}

	@Override
	public void sendTrial(Remind remind, String mainPageUrl) {
		sendAlimtalk(remind, mainPageUrl)
			.exceptionally(e -> {
				log.warn("[NCP] Remind Error Occur : {}", e.getMessage());
				throw new AjajaException(ErrorCode.EXTERNAL_API_FAIL);
			});
	}

	@Async
	public CompletableFuture<String> sendAlimtalk(Remind remind, String feedbackUrl) {
		NaverRequest.Alimtalk request = NaverRequest.Alimtalk.remind(remind.getPhoneNumber(), remind.getTitle(),
			remind.getMessage(), feedbackUrl);

		return CompletableFuture.supplyAsync(alimtalkSupplier(request))
			.thenApply(tries -> {
				log.info("[NCP] Remind Sent To : {} After {} tries", remind.getPhoneNumber(), tries);
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
