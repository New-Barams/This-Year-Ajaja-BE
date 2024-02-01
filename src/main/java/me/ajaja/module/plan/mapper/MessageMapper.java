package me.ajaja.module.plan.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import me.ajaja.module.plan.domain.Message;
import me.ajaja.module.plan.domain.RemindInfo;
import me.ajaja.module.plan.dto.PlanRequest;

@Mapper(componentModel = "spring")
public interface MessageMapper {
	@Mapping(source = "dto.remindMonth", target = "remindMonth")
	@Mapping(source = "dto.remindDay", target = "remindDay")
	List<Message> toDomain(List<PlanRequest.Message> dto);

	RemindInfo toDomain(PlanRequest.UpdateRemind dto);
}
