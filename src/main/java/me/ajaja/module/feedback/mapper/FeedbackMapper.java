package me.ajaja.module.feedback.mapper;

import java.time.ZonedDateTime;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import me.ajaja.global.common.BaseTime;
import me.ajaja.module.feedback.application.model.PlanFeedbackInfo;
import me.ajaja.module.feedback.application.model.UpdatableFeedback;
import me.ajaja.module.feedback.domain.Feedback;
import me.ajaja.module.feedback.dto.FeedbackResponse;
import me.ajaja.module.feedback.infra.FeedbackEntity;
import me.ajaja.module.feedback.infra.model.FeedbackInfo;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
	@Mapping(source = "entity.id", target = "id")
	@Mapping(target = "createdAt", expression = "java(new BaseTime(entity.getCreatedAt()))")
	@Mapping(target = "updatedAt", expression = "java(new BaseTime(entity.getUpdatedAt()))")
	@Mapping(target = "achieve", expression = "java(Achieve.of(entity.getAchieve()))")
	Feedback toDomain(FeedbackEntity entity);

	List<Feedback> toDomain(List<FeedbackEntity> entities);

	@Mapping(target = "achieveRate", expression = "java(findAchieveRate(feedbacks))")
	@Mapping(source = "info.title", target = "title")
	FeedbackResponse.FeedbackInfo toResponse(PlanFeedbackInfo info, List<FeedbackResponse.RemindFeedback> feedbacks);

	@Mapping(target = "deleted", expression = "java(false)")
	@Mapping(source = "info.message", target = "message")
	@Mapping(target = "achieve", expression = "java(feedback.getInfo().getAchieve().getRate())")
	FeedbackEntity toEntity(Feedback feedback);

	default int findAchieveRate(List<FeedbackResponse.RemindFeedback> feedbacks) {
		return (int)feedbacks.stream()
			.mapToInt(FeedbackResponse.RemindFeedback::getAchieve)
			.average()
			.orElse(0);
	}

	@Mapping(target = "reminded", expression = "java(true)")
	@Mapping(target = "remindMonth", expression = "java(sendDate.getMonth())")
	@Mapping(target = "remindDate", expression = "java(sendDate.getDate())")
	@Mapping(target = "endMonth", expression = "java(endDate.getMonthValue())")
	@Mapping(target = "endDate", expression = "java(endDate.getDayOfMonth())")
	FeedbackResponse.RemindFeedback toResponse(BaseTime sendDate, FeedbackInfo feedbackInfo, ZonedDateTime endDate);

	@Mapping(target = "message", expression = "java(\"\")")
	@Mapping(target = "reminded", expression = "java(isReminded(sendDate))")
	@Mapping(target = "remindMonth", expression = "java(sendDate.getMonth())")
	@Mapping(target = "remindDate", expression = "java(sendDate.getDate())")
	@Mapping(target = "endMonth", expression = "java(endDate.getMonthValue())")
	@Mapping(target = "endDate", expression = "java(endDate.getDayOfMonth())")
	@Mapping(target = "achieve", ignore = true)
	FeedbackResponse.RemindFeedback toEmptyResponse(BaseTime sendDate, Integer remindTime, ZonedDateTime endDate);

	default boolean isReminded(BaseTime sendDate) {
		BaseTime now = BaseTime.now();
		return now.isAfter(sendDate);
	}

	@Mapping(target = "remainPeriod", expression = "java(feedback.period().getBetweenDays(BaseTime.now()))")
	@Mapping(target = "month", expression = "java(feedback.period().getMonth())")
	@Mapping(target = "date", expression = "java(feedback.period().getDate())")
	FeedbackResponse.EvaluableFeedback toResponse(UpdatableFeedback feedback);
}
