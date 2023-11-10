package com.newbarams.ajaja.module.plan.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.tag.application.TagService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePlanService {
	private final GetPlanService getPlanService;
	private final TagService tagService;

	public void updatePublicStatus(Long id) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updatePublicStatus();
	}

	public void updateRemindableStatus(Long id) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updateRemindableStatus();
	}

	public PlanResponse.Create update(Long id, PlanRequest.Update request, String date) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.update(
			date,
			request.title(),
			request.description(),
			request.remindTotalPeriod(),
			request.remindTerm(),
			request.remindDate(),
			request.remindTime(),
			request.isPublic(),
			request.isRemindable(),
			tagService.getTags(request.tags()),
			convertToMessage(request.messages())
		);

		return PlanMapper.toResponse(plan);
	}

	private List<Message> convertToMessage(List<String> messages) {
		return messages.stream()
			.map((content) -> new Message(content))
			.collect(Collectors.toList());
	}
}
