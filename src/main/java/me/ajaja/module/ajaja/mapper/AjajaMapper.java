package me.ajaja.module.ajaja.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.Receiver;
import me.ajaja.module.ajaja.domain.Target;
import me.ajaja.module.ajaja.infra.AjajaEntity;
import me.ajaja.module.remind.application.model.RemindableAjaja;

@Mapper(componentModel = "spring")
public interface AjajaMapper {
	@Mapping(target = "type", expression = "java(ajaja.getType())")
	@Mapping(source = "target.targetId", target = "targetId")
	@Mapping(source = "receiver.userId", target = "userId")
	AjajaEntity toEntity(Ajaja ajaja);

	@Mapping(target = "type", expression = "java(Ajaja.Type.valueOf(ajajaEntity.getType().toUpperCase()))")
	@Mapping(source = "ajajaEntity", target = "target", qualifiedByName = "entityToTarget")
	@Mapping(source = "ajajaEntity", target = "receiver", qualifiedByName = "entityToReceiver")
	@Mapping(target = "count", ignore = true)
	Ajaja toDomain(AjajaEntity ajajaEntity);

	@Named("entityToTarget")
	static Target toTarget(AjajaEntity ajaja) {
		return new Target(ajaja.getTargetId(), null);
	}

	@Named("entityToReceiver")
	static Receiver toReceiver(AjajaEntity ajaja) {
		return new Receiver(ajaja.getUserId(), null, null);
	}

	@Mapping(target = "type", expression = "java(Ajaja.Type.PLAN)")
	@Mapping(source = "model", target = "target", qualifiedByName = "modelToTarget")
	@Mapping(source = "model", target = "receiver", qualifiedByName = "modelToReceiver")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "canceled", ignore = true)
	Ajaja toDomain(RemindableAjaja model);

	@Named("modelToTarget")
	static Target toTarget(RemindableAjaja ajaja) {
		return new Target(ajaja.planId(), ajaja.title());
	}

	@Named("modelToReceiver")
	static Receiver toReceiver(RemindableAjaja ajaja) {
		return new Receiver(ajaja.userId(), ajaja.email(), ajaja.phoneNumber());
	}
}
