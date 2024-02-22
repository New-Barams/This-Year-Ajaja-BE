package me.ajaja.infra.feign.ncp.client;

import static org.springframework.http.MediaType.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import me.ajaja.infra.feign.ncp.model.NaverRequest;
import me.ajaja.infra.feign.ncp.model.NaverResponse;

@FeignClient(
	name = "send-alimtalk",
	url = "https://sens.apigw.ntruss.com",
	configuration = NaverFeignHeaderConfiguration.class
)
public interface NaverSendAlimtalkFeignClient {
	@PostMapping(value = "/alimtalk/v2/services/{id}/messages", consumes = APPLICATION_JSON_VALUE)
	NaverResponse.AlimTalk send(@PathVariable("id") String serviceId, @RequestBody NaverRequest.Alimtalk request);
}
