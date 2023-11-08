package com.newbarams.ajaja.module.plan.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.domain.dto.MessageRequest;
import com.newbarams.ajaja.module.plan.domain.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.domain.dto.PlanResponse;
import com.newbarams.ajaja.module.tag.domain.Tag;

@Component
public class PlanMapper {
	public static Plan toEntity(PlanRequest.Create dto, Long userId, Set<Tag> tags) {
		Content content = convertToContent(dto.title(), dto.description());
		RemindInfo info = convertToRemindInfo(dto.remindTotalPeriod(), dto.remindTerm(), dto.remindDate(),
			dto.remindTime());
		List<Message> messages = convertToMessageList(dto.messages());

		return Plan.builder()
			.userId(userId)
			.content(content)
			.info(info)
			.isPublic(dto.isPublic())
			.messages(messages)
			.tags(tags)
			.build();
	}

	private static Content convertToContent(String title, String description) {
		return new Content(title, description);
	}

	private static RemindInfo convertToRemindInfo(int remindTotalPeriod, int remindTerm, int remindDate,
		String remindTime) {
		return new RemindInfo(remindTotalPeriod, remindTerm, remindDate, remindTime);
	}

	private static List<Message> convertToMessageList(List<MessageRequest.Create> requests) {
		if (requests == null) {
			return null;
		}

		List<Message> messages = new ArrayList<>(requests.size());

		for (MessageRequest.Create request : requests) {
			Message message = new Message(request.content(), request.index());
			messages.add(message);
		}

		return messages;
	}

	public static PlanResponse.Create toResponse(Plan plan) {
		return new PlanResponse.Create(
			plan.getId(),
			plan.getUserId(),
			plan.getContent().getTitle(),
			plan.getContent().getDescription(),
			plan.getStatus().isPublic(),
			plan.getAjajas().size(),
			convertTagToDto(plan.getTags()),
			plan.getCreatedAt()
		);
	}

	public static PlanResponse.GetOne toResponse(Plan plan, String username) {
		return new PlanResponse.GetOne(
			plan.getId(),
			plan.getUserId(),
			username,
			plan.getContent().getTitle(),
			plan.getContent().getDescription(),
			plan.getStatus().isPublic(),
			plan.getAjajas().size(),
			convertTagToDto(plan.getTags()),
			plan.getCreatedAt()
		);
	}

	private static List<String> convertTagToDto(Set<Tag> tags) {
		if (tags == null) {
			return null;
		}

		List<String> tagNames = new ArrayList<>();

		for (Tag tag : tags) {
			tagNames.add(tag.getName());
		}

		return tagNames;
	}
}
