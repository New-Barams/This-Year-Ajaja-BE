package com.newbarams.ajaja.module.plan.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;

@Component
public class PlanMapper {
	public static Plan toEntity(PlanRequest.Create dto, Long userId, int month) {
		Content content = toContent(dto.title(), dto.description());
		RemindInfo info = toRemindInfo(dto.remindTotalPeriod(), dto.remindTerm(), dto.remindDate(),
			dto.remindTime());
		List<Message> messages = toMessages(dto.messages());

		return Plan.builder()
			.month(month)
			.userId(userId)
			.content(content)
			.info(info)
			.isPublic(dto.isPublic())
			.iconNumber(dto.iconNumber())
			.messages(messages)
			.build();
	}

	private static Content toContent(String title, String description) {
		return new Content(title, description);
	}

	private static RemindInfo toRemindInfo(int remindTotalPeriod, int remindTerm, int remindDate,
		String remindTime) {
		return new RemindInfo(remindTotalPeriod, remindTerm, remindDate, remindTime);
	}

	public static List<Message> toMessages(List<String> messageList) {
		if (messageList == null) {
			return null;
		}

		List<Message> messages = new ArrayList<>(messageList.size());

		for (String message : messageList) {
			messages.add(new Message(message));
		}

		return messages;
	}

	public static PlanResponse.Create toResponse(Plan plan, List<String> tags) {
		return new PlanResponse.Create(
			plan.getId(),
			plan.getUserId(),
			plan.getContent().getTitle(),
			plan.getContent().getDescription(),
			plan.getStatus().isPublic(),
			plan.getStatus().isCanRemind(),
			plan.getStatus().isCanAjaja(),
			0,
			tags,
			plan.getCreatedAt()
		);
	}

	public static PlanResponse.GetOne toResponse(Plan plan, String nickname, List<String> tags, Long ajajas) {
		return new PlanResponse.GetOne(
			plan.getId(),
			plan.getUserId(),
			nickname,
			plan.getContent().getTitle(),
			plan.getContent().getDescription(),
			plan.getStatus().isPublic(),
			plan.getStatus().isCanRemind(),
			plan.getStatus().isCanAjaja(),
			ajajas,
			tags,
			plan.getCreatedAt()
		);
	}

	public static PlanResponse.GetAll toGetAllResponse(Plan plan, String nickname, List<String> tags, Long ajajas) {
		return new PlanResponse.GetAll(
			plan.getId(),
			plan.getUserId(),
			nickname,
			plan.getContent().getTitle(),
			ajajas,
			tags,
			plan.getCreatedAt()
		);
	}
}
