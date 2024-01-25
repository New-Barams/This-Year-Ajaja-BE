package com.newbarams.ajaja.module.remind.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.remind.application.model.RemindAddress;
import com.newbarams.ajaja.module.remind.application.port.in.SendTestRemindUseCase;
import com.newbarams.ajaja.module.remind.application.port.out.FindRemindAddressPort;
import com.newbarams.ajaja.module.remind.application.port.out.SendAlimtalkRemindPort;
import com.newbarams.ajaja.module.remind.application.port.out.SendEmailRemindPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendTestRemindService implements SendTestRemindUseCase {
	private static final String TEST_PLAN_NAME = "테스트 계획입니다!";
	private static final String TEST_REMIND_MESSAGE = "테스트 메세지입니다!";
	private static final String MAIN_PAGE_URL = "https://www.ajaja.me/home";
	private static final int MAX_SEND_COUNT = 3;
	private static final int INIT_COUNT_VALUE = 1;

	private final FindRemindAddressPort findRemindAddressPort;
	private final SendEmailRemindPort sendEmailRemindPort;
	private final SendAlimtalkRemindPort sendAlimtalkRemindPort;

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public String send(Long userId) {
		RemindAddress address = findRemindAddressPort.findAddressByUserId(userId);
		sendRemind(address);
		return address.type();
	}

	private void sendRemind(RemindAddress address) {
		Integer sendCount = getSendCount(address.userEmail());

		try {
			if (address.type().equals("EMAIL")) {
				sendEmailRemindPort.send(address.remindEmail(), TEST_PLAN_NAME, TEST_REMIND_MESSAGE, MAIN_PAGE_URL);
			} else {
				sendAlimtalkRemindPort.send(address.phoneNumber(), TEST_PLAN_NAME, TEST_REMIND_MESSAGE, MAIN_PAGE_URL);
			}
		} catch (RuntimeException e) {
			log.warn("Send Error {} Occurs : {}", e.getCause(), e.getMessage());
			throw new AjajaException(REMIND_TASK_FAILED);
		}
		increaseCount(address.userEmail(), sendCount);
	}

	private Integer getSendCount(String email) {
		Integer sendCount = (Integer)redisTemplate.opsForValue().get(email);

		if (sendCount != null && sendCount >= MAX_SEND_COUNT) {
			throw new AjajaException(REQUEST_OVER_MAX);
		}
		return sendCount;
	}

	private void increaseCount(String userEmail, Integer sendCount) {
		if (sendCount == null) {
			redisTemplate.opsForValue().set(userEmail, INIT_COUNT_VALUE, Duration.ofDays(1));
			return;
		}
		redisTemplate.opsForValue().increment(userEmail);
	}
}
