package me.ajaja.module.remind.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import me.ajaja.global.common.TimeValue;
import me.ajaja.module.remind.adapter.out.persistence.model.RemindEntity;
import me.ajaja.module.remind.application.model.RemindAddress;
import me.ajaja.module.remind.application.model.RemindMessageInfo;
import me.ajaja.module.remind.domain.Receiver;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.domain.Target;

@Mapper(componentModel = "spring")
public interface RemindMapper {
	@Mapping(source = "entity.content", target = "message")
	@Mapping(source = "entity.remindMonth", target = "remindMonth")
	@Mapping(source = "entity.remindDay", target = "remindDay")
	@Mapping(source = "entity", target = "receiver", qualifiedByName = "toEmptyReceiver")
	@Mapping(source = "entity", target = "target", qualifiedByName = "toEmptyTarget")
	Remind toDomain(RemindEntity entity);

	@Named("toEmptyReceiver")
	static Receiver toUserInfo(RemindEntity entity) {
		return new Receiver(entity.getUserId(), null, null, null);
	}

	@Named("toEmptyTarget")
	static Target toPlanInfo(RemindEntity entity) {
		return new Target(entity.getPlanId(), null);
	}

	List<Remind> toDomain(List<RemindEntity> entities);

	@Mapping(target = "message", expression = "java(info.plan().getMessage(time.getMonth()))")
	@Mapping(source = "info", target = "receiver", qualifiedByName = "toReceiver")
	@Mapping(source = "info", target = "target", qualifiedByName = "toTarget")
	@Mapping(target = "type", ignore = true)
	@Mapping(target = "remindMonth", ignore = true)
	@Mapping(target = "remindDay", ignore = true)
	Remind toDomain(RemindMessageInfo info, TimeValue time);

	@Named("toReceiver")
	static Receiver toUserInfo(RemindMessageInfo info) {
		return new Receiver(info.plan().getUserId(), info.remindType(), info.email(), info.phoneNumber());
	}

	@Named("toTarget")
	static Target toPlanInfo(RemindMessageInfo info) {
		return new Target(info.plan().getId(), info.plan().getPlanTitle());
	}

	@Mapping(source = "address", target = "receiver", qualifiedByName = "toReceiverAddress")
	@Mapping(source = "address", target = "target", qualifiedByName = "toTestTarget")
	@Mapping(target = "type", ignore = true)
	@Mapping(target = "message", expression = "java(\"테스트 메세지입니다!\")")
	@Mapping(target = "remindMonth", ignore = true)
	@Mapping(target = "remindDay", ignore = true)
	Remind toMockDomain(RemindAddress address);

	@Named("toReceiverAddress")
	static Receiver toReceiver(RemindAddress address) {
		return new Receiver(address.userId(), address.remindType(), address.remindEmail(), address.phoneNumber());
	}

	@Named("toTestTarget")
	static Target toTarget(RemindAddress address) {
		return new Target(0L, "테스트 계획입니다!");
	}

	@Mapping(source = "receiver.id", target = "userId")
	@Mapping(source = "target.id", target = "planId")
	@Mapping(source = "remindDate.month", target = "remindMonth")
	@Mapping(source = "remindDate.day", target = "remindDay")
	@Mapping(source = "message", target = "content")
	@Mapping(target = "deleted", expression = "java(false)")
	@Mapping(target = "id", ignore = true)
	RemindEntity toEntity(Remind remind);
}
