package me.ajaja.infra.feign.ncp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

public final class NaverResponse {
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AlimTalk {
		private final String requestId;
		private final String statusCode;
		private final List<Message> messages;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Message {
		private final String messageId;
		private final String to;
		private final String content;
		private final String requestStatusCode;
		private final String requestStatusName;
		private final String requestStatusDesc;
	}
}
