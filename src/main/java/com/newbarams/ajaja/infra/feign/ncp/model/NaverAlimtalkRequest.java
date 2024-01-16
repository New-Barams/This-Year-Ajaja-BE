package com.newbarams.ajaja.infra.feign.ncp.model;

import static com.newbarams.ajaja.infra.feign.ncp.model.AlimtalkTemplate.*;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.newbarams.ajaja.global.common.TimeValue;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverAlimtalkRequest {
	private static final String PLAN_BASE_URL = "https://www.ajaja.me/plans/";
	private static final String CHANNEL = "@올해도아좌좌";

	private final String plusFriendId;
	private final String templateCode;
	private final List<NaverAlimtalkMessage> messages;

	private NaverAlimtalkRequest(String templateCode, List<NaverAlimtalkMessage> messages) {
		this.plusFriendId = CHANNEL;
		this.templateCode = templateCode;
		this.messages = messages;
	}

	public static NaverAlimtalkRequest remind(String to, String planName, String remindMessage, String feedbackUrl) {
		ZonedDateTime deadLine = TimeValue.now().oneMonthLater();
		String content = REMIND.content(planName, remindMessage, deadLine.getMonthValue(), deadLine.getDayOfMonth());
		return of(REMIND.getTemplateCode(), to, content, feedbackUrl);
	}

	public static NaverAlimtalkRequest ajaja(String to, Long ajajaCount, String planName, Long planId) {
		String content = AJAJA.content(ajajaCount, planName);
		return of(AJAJA.getTemplateCode(), to, content, PLAN_BASE_URL + planId);
	}

	private static NaverAlimtalkRequest of(String templateCode, String to, String content, String buttonUrl) {
		NaverAlimtalkMessage message = new NaverAlimtalkMessage(to, content, buttonUrl);
		return new NaverAlimtalkRequest(templateCode, List.of(message));
	}

	// multi-request
	public static NaverAlimtalkRequest reminds(List<NaverAlimtalkMessage> messages) {
		return new NaverAlimtalkRequest(REMIND.getTemplateCode(), messages);
	}

	public static NaverAlimtalkRequest ajajas(List<NaverAlimtalkMessage> messages) {
		return new NaverAlimtalkRequest(AJAJA.getTemplateCode(), messages);
	}
}
