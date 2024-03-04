package me.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.BaseTime;
import me.ajaja.module.remind.application.port.out.SaveRemindPort;
import me.ajaja.module.remind.domain.Remind;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateRemindService { // todo: plan remind만 저장한다면 네이밍이 이게 맞을까?
	private final SaveRemindPort saveRemindPort;

	public void create(Remind send, BaseTime time, String endPoint) {
		Remind remind = Remind.plan(send.getUserId(), endPoint, send.getPlanId(), send.getMessage(), time);
		saveRemindPort.save(remind);
	}
}
