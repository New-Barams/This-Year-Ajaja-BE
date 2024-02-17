package me.ajaja.module.footprint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;

import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;
import me.ajaja.module.footprint.adapter.out.persistence.model.TargetEntity;
import me.ajaja.module.footprint.adapter.out.persistence.model.WriterEntity;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FreeFootprint;
import me.ajaja.module.footprint.domain.KptFootprint;
import me.ajaja.module.footprint.domain.Target;
import me.ajaja.module.footprint.domain.Title;
import me.ajaja.module.footprint.domain.Writer;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FootprintMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "target.id", target = "targetId")
	@Mapping(source = "writer.id", target = "writerId")
	@Mapping(source = "title.title", target = "title")
	@Mapping(source = "visible", target = "visible")
	@Mapping(source = "deleted", target = "deleted")
	@Mapping(target = "type", expression = "java(toType(footprint))")
	@Mapping(target = "content", expression = "java(toContent(footprint))")
	@Mapping(target = "keepContent", expression = "java(toKeepContent(footprint))")
	@Mapping(target = "problemContent", expression = "java(toProblemContent(footprint))")
	@Mapping(target = "tryContent", expression = "java(toTryContent(footprint))")
	FootprintEntity toEntity(Footprint footprint);

	default String toType(Footprint footprint) {
		return (footprint instanceof FreeFootprint) ? "FREE" : "KPT";
	}

	default String toContent(Footprint footprint) {
		return (footprint instanceof FreeFootprint) ? ((FreeFootprint)footprint).getContent() : null;
	}

	default String toKeepContent(Footprint footprint) {
		return (footprint instanceof KptFootprint) ? ((KptFootprint)footprint).getKeepContent() : null;
	}

	default String toProblemContent(Footprint footprint) {
		return (footprint instanceof KptFootprint) ? ((KptFootprint)footprint).getProblemContent() : null;
	}

	default String toTryContent(Footprint footprint) {
		return (footprint instanceof KptFootprint) ? ((KptFootprint)footprint).getTryContent() : null;
	}

	Footprint toDomain(FootprintEntity footprintEntity, TargetEntity targetEntity, WriterEntity writerEntity);

	@ObjectFactory
	default Footprint createFootprint(FootprintEntity footprintEntity, TargetEntity targetEntity,
		WriterEntity writerEntity) {
		if (footprintEntity.getType().equals("FREE")) {
			return new FreeFootprint(
				footprintEntity.getId(),
				new Target(targetEntity.id(), targetEntity.title()),
				new Writer(writerEntity.id(), writerEntity.nickname()),
				new Title(footprintEntity.getTitle()),
				footprintEntity.isVisible(),
				footprintEntity.isDeleted(),
				null,
				footprintEntity.getContent()
			);
		} else {
			return new KptFootprint(
				footprintEntity.getId(),
				new Target(targetEntity.id(), targetEntity.title()),
				new Writer(writerEntity.id(), writerEntity.nickname()),
				new Title(footprintEntity.getTitle()),
				footprintEntity.isVisible(),
				footprintEntity.isDeleted(),
				null,
				footprintEntity.getKeepContent(),
				footprintEntity.getProblemContent(),
				footprintEntity.getTryContent()
			);
		}
	}
}
