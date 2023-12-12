package com.newbarams.ajaja.module.remind.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.infra.RemindEntity;

@Mapper(componentModel = "spring")
public interface RemindMapper {
	@Mapping(source = "entity.content", target = "info.content")
	@Mapping(source = "entity.starts", target = "period.starts")
	@Mapping(source = "entity.ends", target = "period.ends")
	Remind toDomain(RemindEntity entity);

	@Mapping(source = "remind.info.content", target = "content")
	@Mapping(source = "remind.period.starts", target = "starts")
	@Mapping(source = "remind.period.ends", target = "ends")
	@Mapping(target = "deleted", expression = "java(false)")
	RemindEntity toEntity(Remind remind);
}
