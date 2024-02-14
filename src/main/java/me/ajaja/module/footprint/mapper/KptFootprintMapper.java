package me.ajaja.module.footprint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;
import me.ajaja.module.footprint.domain.KptFootprint;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KptFootprintMapper extends FootprintMapper {
	@FootprintMapper.ToEntity
	@Mapping(target = "type", constant = "KPT")
	@Mapping(source = "keepContent", target = "keepContent")
	@Mapping(source = "problemContent", target = "problemContent")
	@Mapping(source = "tryContent", target = "tryContent")
	FootprintEntity toEntity(KptFootprint footprint);

	@FootprintMapper.ToDomain
	@Mapping(source = "keepContent", target = "keepContent")
	@Mapping(source = "problemContent", target = "problemContent")
	@Mapping(source = "tryContent", target = "tryContent")
	KptFootprint toDomain(FootprintEntity footprintEntity);
}
