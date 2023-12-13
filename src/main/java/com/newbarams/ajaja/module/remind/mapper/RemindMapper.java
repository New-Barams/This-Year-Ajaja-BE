package com.newbarams.ajaja.module.remind.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.infra.RemindEntity;

@Mapper(componentModel = "spring")
public interface RemindMapper {
	@Mapping(source = "entity.content", target = "info.content")
	@Mapping(target = "remindDate", expression = "java(entity.getCreatedAt())")
	Remind toDomain(RemindEntity entity);

	List<Remind> toDomain(List<RemindEntity> entities);

	@Mapping(source = "remind.info.content", target = "content")
	@Mapping(target = "deleted", expression = "java(false)")
	RemindEntity toEntity(Remind remind);
}
