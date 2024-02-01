package me.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.remind.application.port.out.SaveRemindPort;
import me.ajaja.module.remind.domain.Remind;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateRemindService {
	private final SaveRemindPort saveRemindPort;

	public void create(Remind send, TimeValue time) {
		Remind remind
			= Remind.plan(send.getUserId(), send.getPlanId(), send.getMessage(), time.getMonth(), time.getDate());
		saveRemindPort.save(remind);
	}
}
