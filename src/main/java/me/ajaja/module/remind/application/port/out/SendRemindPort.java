package me.ajaja.module.remind.application.port.out;

import me.ajaja.infra.feign.ncp.model.NaverRequest;
import me.ajaja.infra.feign.ncp.model.NaverResponse;

public interface SendRemindPort {
	NaverResponse.AlimTalk send(NaverRequest.Alimtalk request);
}
