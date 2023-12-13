package com.newbarams.ajaja.module.plan.mapper;

import org.mapstruct.Mapper;

import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;

@Mapper(componentModel = "spring")
public interface MessageMapper {
	Message toDomain(PlanRequest.CreateMessage dto);
}
