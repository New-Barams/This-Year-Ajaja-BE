package me.ajaja.module.remind.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.remind.application.port.in.SendTrialRemindUseCase;
import me.ajaja.module.remind.application.port.out.FindRemindAddressPort;
import me.ajaja.module.remind.domain.Remind;

@Service
@RequiredArgsConstructor
class SendTrialRemindService implements SendTrialRemindUseCase {
	private final FindRemindAddressPort findRemindAddressPort;
	private final SendRemindStrategyFactory strategyFactory;
	private final SendTrialCounter sendTrialCounter;

	@Override
	public String send(Long userId) {
		Remind remind = findRemindAddressPort.findRemindByUserId(userId);

		sendTrialCounter.count(remind.getPhoneNumber());

		var strategy = strategyFactory.get(remind.getRemindType());
		strategy.sendTrial(remind);

		return remind.getRemindType();
	}
}
