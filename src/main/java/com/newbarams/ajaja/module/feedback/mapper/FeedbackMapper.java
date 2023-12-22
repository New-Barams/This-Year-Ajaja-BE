package com.newbarams.ajaja.module.feedback.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.dto.FeedbackResponse;
import com.newbarams.ajaja.module.feedback.infra.FeedbackEntity;
import com.newbarams.ajaja.module.feedback.infra.model.FeedbackInfo;
import com.newbarams.ajaja.module.plan.domain.Plan;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
	@Mapping(source = "entity.id", target = "id")
	@Mapping(target = "createdAt", expression = "java(new TimeValue(entity.getCreatedAt()))")
	@Mapping(target = "updatedAt", expression = "java(new TimeValue(entity.getUpdatedAt()))")
	@Mapping(target = "achieve", expression = "java(Achieve.of(entity.getAchieve()))")
	Feedback toDomain(FeedbackEntity entity);

	List<Feedback> toDomain(List<FeedbackEntity> entities);

	@Mapping(target = "deleted", expression = "java(false)")
	@Mapping(source = "info.message", target = "message")
	@Mapping(target = "achieve", expression = "java(feedback.getInfo().getAchieve().getRate())")
	FeedbackEntity toEntity(Feedback feedback);

	@Mapping(target = "feedbacks", expression = "java(toResponse(feedbackInfos))")
	@Mapping(target = "achieveRate", expression = "java(findAchieveRate(feedbackInfos))")
	@Mapping(source = "plan.content.title", target = "planName")
	@Mapping(source = "plan.info.remindTotalPeriod", target = "totalPeriod")
	@Mapping(source = "plan.info.remindTerm", target = "remindTerm")
	@Mapping(source = "plan.info.remindDate", target = "remindDay")
	FeedbackResponse.FeedbackInfo toResponse(Plan plan, List<FeedbackInfo> feedbackInfos);

	default int findAchieveRate(List<FeedbackInfo> feedbackInfos) {
		return (int)feedbackInfos.stream()
			.mapToInt(FeedbackInfo::achieve)
			.average()
			.orElse(0);
	}

	List<FeedbackResponse.RemindedFeedback> toResponse(List<FeedbackInfo> feedbackInfos);

	FeedbackResponse.RemindedFeedback toResponse(FeedbackInfo feedbackInfo);
}
