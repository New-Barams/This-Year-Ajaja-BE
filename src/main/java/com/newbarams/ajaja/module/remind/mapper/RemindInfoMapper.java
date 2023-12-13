package com.newbarams.ajaja.module.remind.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;
import com.newbarams.ajaja.module.remind.infra.RemindEntity;

@Mapper(
	componentModel = "spring",
	nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface RemindInfoMapper {
	@Mapping(source = "content", target = "remindMessage")
	@Mapping(target = "isReminded", expression = "java(true)")
	RemindResponse.Response toSentMessages(RemindEntity entity);

	List<RemindResponse.Response> toSentMessages(List<RemindEntity> entities);

	@Mapping(source = "content", target = "remindMessage")
	RemindResponse.Response toFutureMessages(Message message);
}
