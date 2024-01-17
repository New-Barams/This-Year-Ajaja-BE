package com.newbarams.ajaja.module.remind.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.remind.adapter.out.persistence.model.RemindEntity;
import com.newbarams.ajaja.module.remind.application.model.RemindMessageInfo;
import com.newbarams.ajaja.module.remind.domain.PlanInfo;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.UserInfo;

@Mapper(componentModel = "spring")
public interface RemindMapper {
	@Mapping(source = "entity.content", target = "message")
	@Mapping(source = "entity.remindMonth", target = "remindMonth")
	@Mapping(source = "entity.remindDay", target = "remindDay")
	@Mapping(source = "entity", target = "userInfo", qualifiedByName = "toNullUserInfo")
	@Mapping(source = "entity", target = "planInfo", qualifiedByName = "toNullPlanInfo")
	Remind toDomain(RemindEntity entity);

	@Named("toNullUserInfo")
	static UserInfo toUserInfo(RemindEntity entity) {
		return new UserInfo(entity.getUserId(), null);
	}

	@Named("toNullPlanInfo")
	static PlanInfo toPlanInfo(RemindEntity entity) {
		return new PlanInfo(entity.getPlanId(), null);
	}

	List<Remind> toDomain(List<RemindEntity> entities);

	@Mapping(target = "message", expression = "java(info.plan().getMessage(time.getMonth()))")
	@Mapping(source = "info", target = "userInfo", qualifiedByName = "toUserInfo")
	@Mapping(source = "info", target = "planInfo", qualifiedByName = "toPlanInfo")
	@Mapping(target = "type", ignore = true)
	@Mapping(target = "remindMonth", ignore = true)
	@Mapping(target = "remindDay", ignore = true)
	Remind toDomain(RemindMessageInfo info, TimeValue time);

	@Named("toUserInfo")
	static UserInfo toUserInfo(RemindMessageInfo info) {
		return new UserInfo(info.plan().getUserId(), info.email());
	}

	@Named("toPlanInfo")
	static PlanInfo toPlanInfo(RemindMessageInfo info) {
		return new PlanInfo(info.plan().getId(), info.plan().getPlanTitle());
	}

	@Mapping(source = "planInfo.id", target = "planId")
	@Mapping(source = "userInfo.id", target = "userId")
	@Mapping(source = "remindDate.month", target = "remindMonth")
	@Mapping(source = "remindDate.day", target = "remindDay")
	@Mapping(source = "message", target = "content")
	@Mapping(target = "deleted", expression = "java(false)")
	@Mapping(target = "id", ignore = true)
	RemindEntity toEntity(Remind remind);
}
