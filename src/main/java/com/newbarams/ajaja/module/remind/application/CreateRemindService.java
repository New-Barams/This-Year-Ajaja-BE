package com.newbarams.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.remind.application.port.out.SaveRemindPort;
import com.newbarams.ajaja.module.remind.domain.Remind;

import lombok.RequiredArgsConstructor;

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
