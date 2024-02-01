package me.ajaja.module.ajaja.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.infra.AjajaEntity;

@Mapper(componentModel = "spring")
public interface AjajaMapper {
	@Mapping(target = "type", expression = "java(ajaja.getType())")
	AjajaEntity toEntity(Ajaja ajaja);

	@Mapping(target = "type", expression = "java(Ajaja.Type.valueOf(ajajaEntity.getType().toUpperCase()))")
	Ajaja toDomain(AjajaEntity ajajaEntity);
}
