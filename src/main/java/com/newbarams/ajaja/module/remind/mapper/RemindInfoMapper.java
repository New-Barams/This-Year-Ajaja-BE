package com.newbarams.ajaja.module.remind.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;
import com.newbarams.ajaja.module.remind.infra.RemindEntity;

@Mapper(
	componentModel = "spring"
)
public interface RemindInfoMapper {
	@Mapping(source = "content", target = "remindMessage")
	@Mapping(target = "isReminded", expression = "java(true)")
	RemindResponse.Messages toSentMessages(RemindEntity entity);

	List<RemindResponse.Messages> toSentMessages(List<RemindEntity> entities);

	@Mapping(source = "remindDate.remindMonth", target = "remindMonth")
	@Mapping(source = "remindDate.remindDay", target = "remindDay")
	@Mapping(source = "content", target = "remindMessage")
	@Mapping(target = "isReminded", expression = "java(false)")
	RemindResponse.Messages toFutureMessages(Message message);
}
