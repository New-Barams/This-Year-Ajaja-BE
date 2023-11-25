package com.newbarams.ajaja.module.remind.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.dto.GetRemindInfo;

@Component
public class RemindMapper {

	public GetRemindInfo.CommonResponse toFutureRemind(
		Plan plan
	) {
		List<GetRemindInfo.FutureRemindResponse> futureRemindResponses = new ArrayList<>();

		int remindTerm = plan.getRemindTerm();
		int remindMonth = plan.getRemindMonth();
		List<Message> messages = plan.getMessages();

		for (Message message : messages) {
			futureRemindResponses.add(
				new GetRemindInfo.FutureRemindResponse(
					message.getContent(),
					remindMonth,
					plan.getRemindDate()
				));

			remindMonth += remindTerm;
		}

		return new GetRemindInfo.CommonResponse(plan, futureRemindResponses);
	}
}
