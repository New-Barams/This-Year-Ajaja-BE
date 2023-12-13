package com.newbarams.ajaja.module.remind.mapper;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

@Mapper(
	componentModel = "spring",
	nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface RemindInfoMapper {
	default List<RemindResponse.Response> toSentMessages(
		List<Remind> reminds,
		List<Feedback> feedbacks
	) {
		return IntStream.range(0, reminds.size()).mapToObj(i -> {
			Remind remind = reminds.get(i);
			Feedback feedback = feedbacks.get(i);
			TimeValue remindTime = new TimeValue(remind.getRemindDate());
			ZonedDateTime deadline = remindTime.oneMonthLater();

			return new RemindResponse.Response(
				feedback.getId(),
				remind.getContent(),
				remindTime.getMonth(),
				remindTime.getDate(),
				feedback.getRate(),
				feedback.isFeedback(),
				feedback.checkDeadline(),
				true,
				deadline.getMonthValue(),
				deadline.getDayOfMonth());
		}).toList();
	}

	@Mapping(target = "remindMessage", expression = "java(\"\")")
	@Mapping(target = "feedbackId", expression = "java(0L)")
	RemindResponse.Response toFutureMessages(Integer remindMonth, Integer remindDate);

	@Mapping(target = "feedbackId", expression = "java(0L)")
	RemindResponse.Response toFutureMessages(Integer remindMonth, Integer remindDate, String remindMessage);
}
