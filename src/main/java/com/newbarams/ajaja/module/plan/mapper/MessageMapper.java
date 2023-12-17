package com.newbarams.ajaja.module.plan.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;

@Mapper(componentModel = "spring")
public interface MessageMapper {
	@Mapping(source = "dto.remindMonth", target = "remindMonth")
	@Mapping(source = "dto.remindDay", target = "remindDay")
	List<Message> toDomain(List<PlanRequest.CreateMessage> dto);
}
