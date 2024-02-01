package me.ajaja.module.plan.dto;

import java.util.List;

import lombok.Data;
import me.ajaja.module.plan.domain.Content;
import me.ajaja.module.plan.domain.Message;
import me.ajaja.module.plan.domain.PlanStatus;
import me.ajaja.module.plan.domain.RemindInfo;

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
