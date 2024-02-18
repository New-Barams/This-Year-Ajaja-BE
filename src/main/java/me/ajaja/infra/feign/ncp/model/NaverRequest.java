package me.ajaja.infra.feign.ncp.model;

import static me.ajaja.infra.feign.ncp.model.AlimtalkTemplate.*;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ajaja.global.common.TimeValue;

public final class NaverRequest {
	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Alimtalk {
		private static final String FEEDBACK_BUTTON_NAME = "피드백 하러가기";
		private static final String PLAN_BUTTON_NAME = "계획 확인하기";
		private static final String PLAN_BASE_URL = "https://www.ajaja.me/plans/";
		private static final String CHANNEL = "@올해도아좌좌";

		private final String plusFriendId;
		private final String templateCode;
		private final List<Message> messages;

		private Alimtalk(String templateCode, List<Message> messages) {
			this(CHANNEL, templateCode, messages);
		}

		public static Alimtalk remind(String to, String planName, String message, String feedbackUrl) {
			ZonedDateTime deadLine = TimeValue.now().oneMonthLater();
			String content = REMIND.content(planName, message, deadLine.getMonthValue(), deadLine.getDayOfMonth());
			return of(REMIND.getTemplateCode(), to, content, feedbackUrl, FEEDBACK_BUTTON_NAME);
		}

		public static Alimtalk ajaja(String to, Long ajajaCount, String planName, Long planId) {
			String content = AJAJA.content(ajajaCount, planName);
			return of(AJAJA.getTemplateCode(), to, content, PLAN_BASE_URL + planId, PLAN_BUTTON_NAME);
		}

		private static Alimtalk of(
			String templateCode, String to, String content, String buttonUrl, String buttonName
		) {
			Message message = new Message(to, content, buttonUrl, buttonName);
			return new Alimtalk(templateCode, List.of(message));
		}

		// multi-request
		public static Alimtalk reminds(List<Message> messages) {
			return new Alimtalk(REMIND.getTemplateCode(), messages);
		}

		public static Alimtalk ajajas(List<Message> messages) {
			return new Alimtalk(AJAJA.getTemplateCode(), messages);
		}
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Message {
		private final String to;
		private final String content;
		private final List<Button> buttons;

		public Message(String to, String content, String link, String buttonName) {
			this.to = to;
			this.content = content;
			this.buttons = List.of(new Button(link, buttonName));
		}

		@Getter
		@JsonIgnoreProperties(ignoreUnknown = true)
		private static class Button {
			private static final String WEB_LINK_BUTTON_TYPE = "WL";

			private final String type;
			private final String name;
			private final String linkMobile;
			private final String linkPc;

			private Button(String link, String buttonName) {
				this.type = WEB_LINK_BUTTON_TYPE;
				this.name = buttonName;
				this.linkMobile = link;
				this.linkPc = link;
			}
		}
	}
}
