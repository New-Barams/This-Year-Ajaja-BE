package com.newbarams.ajaja.module.remind.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

@Component
public class RemindMapper {

	public RemindResponse.CommonResponse toFutureRemind(
		Plan plan
	) {
		List<RemindResponse.FutureResponse> futureResponses = new ArrayList<>();

		int remindTerm = plan.getRemindTerm();
		int remindMonth = plan.getRemindMonth();
		List<Message> messages = plan.getMessages();

		for (Message message : messages) {
			futureResponses.add(
				new RemindResponse.FutureResponse(
					0L,
					message.getContent(),
					remindMonth,
					plan.getRemindDate(),
					0,
					false,
					false,
					false,
					0,
					0
				));

			remindMonth += remindTerm;
		}

		return new RemindResponse.CommonResponse(
			plan.getRemindTimeName(),
			plan.getRemindDate(),
			plan.getRemindTerm(),
			plan.getRemindTotalPeriod(),
			plan.getIsRemindable(),
			Collections.EMPTY_LIST,
			futureResponses
		);
	}
}
