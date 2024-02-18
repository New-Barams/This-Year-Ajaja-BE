package me.ajaja.module.remind.application;

import static me.ajaja.global.exception.ErrorCode.*;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.remind.application.model.RemindStrategyFactory;
import me.ajaja.module.remind.application.port.in.SendTestRemindUseCase;
import me.ajaja.module.remind.application.port.out.FindRemindAddressPort;
import me.ajaja.module.remind.domain.Remind;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendTestRemindService implements SendTestRemindUseCase {
	private static final String MAIN_PAGE_URL = "https://www.ajaja.me/home";
	private static final int MAX_SEND_COUNT = 3;
	private static final int INIT_COUNT_VALUE = 1;

	private final FindRemindAddressPort findRemindAddressPort;
	private final RemindStrategyFactory factory;

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public String send(Long userId) {
		Remind address = findRemindAddressPort.findAddressByUserId(userId);
		sendRemind(address);
		return address.getRemindType();
	}

	private void sendRemind(Remind address) {
		Integer sendCount = getSendCount(address.getPhoneNumber());
		factory.get(address.getRemindType()).sendTrial(address, MAIN_PAGE_URL);
		increaseCount(address.getPhoneNumber(), sendCount);
	}

	private Integer getSendCount(String phoneNumber) {
		Integer sendCount = (Integer)redisTemplate.opsForValue().get(phoneNumber);

		if (sendCount != null && sendCount >= MAX_SEND_COUNT) {
			throw new AjajaException(REQUEST_OVER_MAX);
		}
		return sendCount;
	}

	private void increaseCount(String phoneNumber, Integer sendCount) {
		if (sendCount == null) {
			redisTemplate.opsForValue().set(phoneNumber, INIT_COUNT_VALUE, Duration.ofDays(1));
			return;
		}
		redisTemplate.opsForValue().increment(phoneNumber);
	}
}
