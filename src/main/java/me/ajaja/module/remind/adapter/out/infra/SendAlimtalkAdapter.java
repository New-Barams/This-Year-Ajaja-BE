package me.ajaja.module.remind.adapter.out.infra;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;
import me.ajaja.infra.feign.ncp.client.NaverCloudProperties;
import me.ajaja.infra.feign.ncp.client.NaverSendAlimtalkFeignClient;
import me.ajaja.infra.feign.ncp.model.NaverRequest;
import me.ajaja.infra.feign.ncp.model.NaverResponse;
import me.ajaja.module.remind.application.CreateRemindService;
import me.ajaja.module.remind.application.port.out.FindRemindableTargetPort;
import me.ajaja.module.remind.application.port.out.SendRemindPort;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.util.RemindExceptionHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendAlimtalkAdapter implements SendRemindPort {
	private static final List<String> HANDLING_ERROR_CODES = List.of("400", "401", "403", "404", "500");
	private static final String FEEDBACK_URL
		= "https://www.ajaja.me/feedback/evaluate?title=%s&month=%d&day=%d&planId=%d";
	private static final String END_POINT = "KAKAO";
	private static final int RETRY_MAX_COUNT = 5;

	private final FindRemindableTargetPort findRemindableTargetPort;
	private final NaverSendAlimtalkFeignClient naverSendAlimtalkFeignClient;
	private final NaverCloudProperties naverCloudProperties;
	private final RemindExceptionHandler exceptionHandler;
	private final CreateRemindService createRemindService;

	@Override
	public String send(String remindTime, TimeValue now) {
		List<Remind> reminds = findRemindableTargetPort.findAllRemindablePlan(remindTime, END_POINT, now);
		reminds.stream().filter(remind -> !Objects.equals(remind.getPhoneNumber(), "01000000000")).forEach(remind -> {
			String url = FEEDBACK_URL.formatted(remind.getTitle(), now.getMonth(), now.getDate(), remind.getPlanId());
			sendAlimtalk(remind, url).exceptionally(e -> {
				log.warn("[NCP] Remind Error Occur : {}", e.getMessage());
				throw new AjajaException(ErrorCode.EXTERNAL_API_FAIL);
			}).thenRun(() -> createRemindService.create(remind, now));
		});

		return END_POINT;
	}

	@Override
	public String sendTest(Remind remind, String mainPageUrl) {
		sendAlimtalk(remind, mainPageUrl)
			.exceptionally(e -> {
				log.warn("[NCP] Remind Error Occur : {}", e.getMessage());
				throw new AjajaException(ErrorCode.EXTERNAL_API_FAIL);
			});

		return END_POINT;
	}

	@Async
	public CompletableFuture<Void> sendAlimtalk(Remind remind, String feedbackUrl) {
		NaverRequest.Alimtalk request = NaverRequest.Alimtalk.remind(remind.getPhoneNumber(), remind.getTitle(),
			remind.getMessage(), feedbackUrl);

		return CompletableFuture.supplyAsync(alimtalkSupplier(request))
			.thenAccept(tries -> log.info("[NCP] Remind Sent To : {} After {} tries", remind.getPhoneNumber(), tries));
	}

	private Supplier<Integer> alimtalkSupplier(NaverRequest.Alimtalk request) {
		return () -> {
			int tries = 1;
			while (tries <= RETRY_MAX_COUNT) {
				NaverResponse.AlimTalk response = naverSendAlimtalkFeignClient.send(naverCloudProperties.getServiceId(),
					request);

				if (isExceptionOccur(response.getStatusCode(), tries)) {
					exceptionHandler.handleRemindException(Integer.parseInt(response.getStatusCode()), tries);
					tries++;
					continue;
				}
				break;
			}
			return tries;
		};
	}

	private boolean isExceptionOccur(String errorCode, int tries) {
		if (tries == RETRY_MAX_COUNT) {
			throw new AjajaException(ErrorCode.EXTERNAL_API_FAIL);
		}
		return HANDLING_ERROR_CODES.contains(errorCode);
	}
}
