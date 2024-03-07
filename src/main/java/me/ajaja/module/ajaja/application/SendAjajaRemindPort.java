package me.ajaja.module.ajaja.application;

import me.ajaja.infra.feign.ncp.model.NaverRequest;
import me.ajaja.infra.feign.ncp.model.NaverResponse;

public interface SendAjajaRemindPort {
	NaverResponse.AlimTalk send(NaverRequest.Alimtalk request);
}
