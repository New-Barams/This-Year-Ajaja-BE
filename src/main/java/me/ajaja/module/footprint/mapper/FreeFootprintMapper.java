package me.ajaja.module.footprint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;
import me.ajaja.module.footprint.domain.FreeFootprint;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FreeFootprintMapper extends FootprintMapper {
	@FootprintMapper.ToEntity
	@Mapping(target = "type", constant = "FREE")
	@Mapping(source = "content", target = "content")
	FootprintEntity toEntity(FreeFootprint footprint);

	@FootprintMapper.ToDomain
	@Mapping(source = "content", target = "content")
	FreeFootprint toDomain(FootprintEntity footprintEntity);
}
