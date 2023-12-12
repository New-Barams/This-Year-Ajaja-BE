package com.newbarams.ajaja.module.feedback.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.infra.FeedbackEntity;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
	@Mapping(source = "entity.id", target = "id")
	@Mapping(target = "createdAt", expression = "java(entity.getCreatedAt())")
	@Mapping(target = "updatedAt", expression = "java(entity.getUpdatedAt())")
	Feedback toDomain(FeedbackEntity entity);

	List<Feedback> toDomain(List<FeedbackEntity> entities);

	@Mapping(target = "deleted", expression = "java(false)")
	FeedbackEntity toEntity(Feedback feedback);
}
