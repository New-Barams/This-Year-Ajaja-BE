package com.newbarams.ajaja.module.plan.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;

@Mapper(componentModel = "spring")
public interface MessageMapper {
	List<Message> toDomain(List<PlanRequest.CreateMessage> dto);
}
