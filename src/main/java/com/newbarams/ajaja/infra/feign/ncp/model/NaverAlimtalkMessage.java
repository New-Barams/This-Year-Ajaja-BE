package com.newbarams.ajaja.infra.feign.ncp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverAlimtalkMessage {
	private final String to;
	private final String content;
	private final List<Button> buttons;

	public NaverAlimtalkMessage(String to, String content, String link) {
		this.to = to;
		this.content = content;
		this.buttons = List.of(new Button(link));
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Button {
		private static final String WEB_LINK_BUTTON_TYPE = "WL";
		private static final String BUTTON_NAME = "피드백 하러가기";

		private final String type;
		private final String name;
		private final String linkMobile;
		private final String linkPc;

		private Button(String link) {
			this.type = WEB_LINK_BUTTON_TYPE;
			this.name = BUTTON_NAME;
			this.linkMobile = link;
			this.linkPc = link;
		}
	}
}
