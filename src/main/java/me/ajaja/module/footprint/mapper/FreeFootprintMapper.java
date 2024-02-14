package me.ajaja.module.footprint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;
import me.ajaja.module.footprint.domain.FreeFootprint;

@Mapper(componentModel = "spring")
public interface FreeFootprintMapper extends FootprintMapper {
	@FootprintMapper.ToEntity
	@Mapping(target = "type", constant = "FREE")
	@Mapping(source = "content", target = "content")
	FootprintEntity toEntity(FreeFootprint footprint);
}
