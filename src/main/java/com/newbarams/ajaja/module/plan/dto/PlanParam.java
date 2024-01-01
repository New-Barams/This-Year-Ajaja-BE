package com.newbarams.ajaja.module.plan.dto;

import java.util.List;

import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.PlanStatus;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;

import lombok.Data;

public class PlanParam {
	@Data
	public static class Create {
		private final int month;
		private final Long userId;
		private final Content content;
		private final RemindInfo info;
		private final PlanStatus status;
		private final int iconNumber;
		private final List<Message> messages;
	}

	@Data
	public static class Update {
		private final int month;
		private final int iconNumber;
		private final Long userId;
		private final Content content;
		private final boolean isPublic;
		private final boolean canAjaja;
	}
}
