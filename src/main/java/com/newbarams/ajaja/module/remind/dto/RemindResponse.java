package com.newbarams.ajaja.module.remind.dto;

import java.util.List;

import lombok.Data;

public final class RemindResponse {
	@Data
	public static class RemindInfo {
		private final String remindTime;
		private final boolean remindable;
		private final int totalPeriod;
		private final int remindTerm;
		private final int remindDate;
		private final List<Message> messageResponses;
	}

	@Data
	public static class Message {
		private final String remindMessage;
		private final int remindMonth;
		private final int remindDay;
		private final boolean reminded;
	}
}
