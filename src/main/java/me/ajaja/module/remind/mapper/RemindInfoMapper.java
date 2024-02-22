package me.ajaja.module.remind.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import me.ajaja.global.common.TimeValue;
import me.ajaja.module.plan.domain.Message;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.RemindDate;
import me.ajaja.module.remind.dto.RemindResponse;

@Mapper(componentModel = "spring")
public interface RemindInfoMapper {
	@Mapping(target = "remindTime", expression = "java(plan.getRemindTimeName())")
	@Mapping(source = "plan.status.canRemind", target = "remindable")
	@Mapping(source = "plan.info.remindTotalPeriod", target = "totalPeriod")
	@Mapping(source = "plan.info.remindTerm", target = "remindTerm")
	@Mapping(source = "plan.info.remindDate", target = "remindDate")
	RemindResponse.RemindInfo toRemindInfo(Plan plan, List<RemindResponse.Message> messageResponses);

	@Mapping(target = "reminded", expression = "java(isReminded(message.getRemindDate()))")
	@Mapping(source = "remindDate.remindMonth", target = "remindMonth")
	@Mapping(source = "remindDate.remindDay", target = "remindDay")
	@Mapping(source = "content", target = "remindMessage")
	RemindResponse.Message toMessage(Message message);

	default boolean isReminded(RemindDate date) {
		TimeValue now = TimeValue.now();
		TimeValue sendDate = TimeValue.parse(2024, date.getRemindMonth(), date.getRemindDay(), 9); // todo:수정
		return now.isAfter(sendDate);
	}
}
